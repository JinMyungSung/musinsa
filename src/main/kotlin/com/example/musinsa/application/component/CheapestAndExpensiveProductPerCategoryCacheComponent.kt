package com.example.musinsa.application.component

import com.example.musinsa.application.component.CheapestAndMostExpensiveProductPerBrandCacheComponent.Companion.CATEGORY_SUMMARY_CACHE_V1_KEY
import com.example.musinsa.application.domain.Category
import com.example.musinsa.application.domain.CheapestAndMostExpensiveProductPerCategoryCacheV1
import com.example.musinsa.outbound.redis.RedisStorage
import com.example.musinsa.persistence.repository.BrandRepository
import org.springframework.stereotype.Component

@Component
class CheapestAndExpensiveProductPerCategoryCacheComponent(
    private val redisStorage: RedisStorage,
    private val brandRepository: BrandRepository,
    private val productReadComponent: ProductReadComponent,
) {
    fun getOrDoCache(category: Category): CheapestAndMostExpensiveProductPerCategoryCacheV1 {
        val cache =
            redisStorage.get(
                "$CATEGORY_PRODUCT_CACHE_V1_KEY:${category.id}",
                CheapestAndMostExpensiveProductPerCategoryCacheV1::class.java,
            )
        // 캐시가 있으면 얼리리턴한다.
        if (cache != null) {
            return cache
        }
        val newCache = getFromDB(category)
        setCache(categoryId = category.id, newCache)
        return newCache
    }

    private fun getFromDB(category: Category): CheapestAndMostExpensiveProductPerCategoryCacheV1 {
        // 해당 카테고리 가장 싼 상품
        val cheapestProduct =
            productReadComponent.findFirstByCategoryIdAndUpdTsGteOrderByMainPrice(
                categoryId = category.id,
                ascending = true,
            )

        val newCache =
            if (cheapestProduct == null) {
                // 가장 싼 상품이 없다면 더미 값으로 채운다.
                CheapestAndMostExpensiveProductPerCategoryCacheV1.noProducts(
                    categoryId = category.id,
                    categoryName = category.name,
                )
            } else {
                // 해당 카테고리 가장 비싼 상품
                val mostExpensiveProduct =
                    productReadComponent.findFirstByCategoryIdAndUpdTsGteOrderByMainPrice(
                        categoryId = category.id,
                        ascending = false,
                    )!!

                // 브랜드 조회
                val brandIdBrandNameMap =
                    brandRepository
                        .findByIdIn(ids = listOf(cheapestProduct.brandId, mostExpensiveProduct.brandId))
                        .associate { it.id!! to it.name }

                CheapestAndMostExpensiveProductPerCategoryCacheV1(
                    categoryId = category.id,
                    categoryName = category.name,
                    cheapestBrandId = cheapestProduct.brandId,
                    cheapestBrandName = brandIdBrandNameMap[cheapestProduct.brandId]!!,
                    cheapestProductMainPrice = cheapestProduct.mainPrice,
                    mostExpensiveBrandId = mostExpensiveProduct.brandId,
                    mostExpensiveProductMainPrice = mostExpensiveProduct.mainPrice,
                    mostExpensiveBrandName = brandIdBrandNameMap[mostExpensiveProduct.brandId]!!,
                )
            }
        return newCache
    }

    fun setCache(
        categoryId: Long,
        cheapestAndMostExpensiveProductPerCategoryCacheV1: CheapestAndMostExpensiveProductPerCategoryCacheV1,
    ) {
        redisStorage.set("$CATEGORY_SUMMARY_CACHE_V1_KEY:$categoryId", cheapestAndMostExpensiveProductPerCategoryCacheV1)
    }

    companion object {
        const val CATEGORY_PRODUCT_CACHE_V1_KEY = "CATEGORY_PRODUCT_CACHE_V1_KEY"
    }
}
