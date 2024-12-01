package com.example.musinsa.application.component

import com.example.musinsa.application.domain.CheapestAndMostExpensiveProductPerBrandCacheV1
import com.example.musinsa.outbound.redis.RedisStorage
import org.springframework.stereotype.Component

@Component
class CheapestAndMostExpensiveProductPerBrandCacheComponent(
    private val redisStorage: RedisStorage,
    private val brandReadComponent: BrandReadComponent,
    private val categoryReadComponent: CategoryReadComponent,
    private val productReadComponent: ProductReadComponent,
) {
    fun getOrDoCache(
        brandId: Long,
        categoryId: Long,
    ): CheapestAndMostExpensiveProductPerBrandCacheV1 {
        val cached =
            redisStorage.get(
                "$BRAND_PRODUCT_CACHE_V1_KEY:$categoryId:$brandId",
                CheapestAndMostExpensiveProductPerBrandCacheV1::class.java,
            )
        // 캐시가 있으면 얼리리턴한다.
        if (cached != null) {
            return cached
        }
        // 캐시가 없다면 DB에서 값을 조회후 캐시를 만든다.
        val foundFromDB =
            getBrandCategorySummaryFromDB(
                brandId = brandId,
                categoryId = categoryId,
            )
        redisStorage.set("$BRAND_PRODUCT_CACHE_V1_KEY:$categoryId:$brandId", foundFromDB)
        return foundFromDB
    }

    fun getBrandCategorySummaryFromDB(
        brandId: Long,
        categoryId: Long,
    ): CheapestAndMostExpensiveProductPerBrandCacheV1 {
        val brand = brandReadComponent.findById(brandId)!!
        val category = categoryReadComponent.getByIdOrNull(categoryId)!!
        val cheapestProduct =
            productReadComponent.findFirstByBrandIdAndCategoryIdAndUpdTsGteOrderByMainPrice(
                brandId = brandId,
                categoryId = categoryId,
                ascending = true,
            )

        return if (cheapestProduct == null) {
            CheapestAndMostExpensiveProductPerBrandCacheV1.noProducts(
                brand = brand,
                category = category,
            )
        } else {
            val mostExpensiveProduct =
                productReadComponent.findFirstByBrandIdAndCategoryIdAndUpdTsGteOrderByMainPrice(
                    brandId = brandId,
                    categoryId = categoryId,
                    ascending = false,
                )!!

            CheapestAndMostExpensiveProductPerBrandCacheV1(
                brandId = brandId,
                brandName = brand.name,
                categoryId = categoryId,
                categoryName = category.name,
                cheapestProductId = cheapestProduct.id!!,
                cheapestProductName = cheapestProduct.name,
                cheapestProductMainPrice = cheapestProduct.mainPrice,
                mostExpensiveProductId = mostExpensiveProduct.id!!,
                mostExpensiveProductName = mostExpensiveProduct.name,
                mostExpensiveProductMainPrice = mostExpensiveProduct.mainPrice,
            )
        }
    }

    fun syncBrandCategorySummaryCacheFromDB(
        brandId: Long,
        categoryId: Long,
    ) {
        val foundFromDB = getBrandCategorySummaryFromDB(brandId, categoryId)
        redisStorage.set("$BRAND_PRODUCT_CACHE_V1_KEY:$categoryId:$brandId", foundFromDB)
    }

    companion object {
        const val CATEGORY_SUMMARY_CACHE_V1_KEY = "CATEGORY_DEAL_CACHE_V1_KEY"
        const val BRAND_PRODUCT_CACHE_V1_KEY = "BRAND_PRODUCT_CACHE_V1_KEY"
    }
}
