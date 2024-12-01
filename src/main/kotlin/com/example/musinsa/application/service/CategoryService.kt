package com.example.musinsa.application.service

import com.example.musinsa.application.component.BrandReadComponent
import com.example.musinsa.application.component.CategoryReadComponent
import com.example.musinsa.application.component.CheapestAndExpensiveProductPerCategoryCacheComponent
import com.example.musinsa.application.component.CheapestAndMostExpensiveProductPerBrandCacheComponent
import com.example.musinsa.application.domain.Brand
import com.example.musinsa.application.domain.Category
import com.example.musinsa.application.exception.BadRequestException
import org.springframework.stereotype.Service
import java.text.NumberFormat

@Service
class CategoryService(
    private val categoryReadComponent: CategoryReadComponent,
    private val cheapestAndExpensiveProductPerCategoryCacheComponent: CheapestAndExpensiveProductPerCategoryCacheComponent,
    private val cheapestAndMostExpensiveProductPerBrandCacheComponent: CheapestAndMostExpensiveProductPerBrandCacheComponent,
    private val brandReadComponent: BrandReadComponent,
) {
    fun getHasProductCategories(): List<Category> {
        return categoryReadComponent.hasProductCategories()
    }

    fun getAllCategories(): List<Category> {
        return categoryReadComponent.getAllCategories()
    }

    // 카테고리 별 최저가격인 브랜드와 가격을 조회하고 총액이 얼마인지 반환한다.
    fun getCheapestBrandPerCategory(): GetCheapestBrandPerCategoryResult {
        return GetCheapestBrandPerCategoryResult(
            this.getHasProductCategories()
                .associateWith { category ->
                    val perCategoryCache =
                        cheapestAndExpensiveProductPerCategoryCacheComponent
                            .getOrDoCache(category = category)
                    val cheapestBrandCategoryCache =
                        cheapestAndMostExpensiveProductPerBrandCacheComponent
                            .getOrDoCache(brandId = perCategoryCache.cheapestBrandId, categoryId = perCategoryCache.categoryId)

                    CheapestProduct(
                        categoryId = category.id,
                        categoryName = category.name,
                        brandId = perCategoryCache.cheapestBrandId,
                        brandName = perCategoryCache.cheapestBrandName,
                        cheapestProductId = cheapestBrandCategoryCache.cheapestProductId,
                        cheapestProductName = cheapestBrandCategoryCache.cheapestProductName,
                        cheapestProductMainPrice = cheapestBrandCategoryCache.cheapestProductMainPrice.toLong(),
                    )
                },
        )
    }

    // 동일 브랜드에서 모든 카테고리를 구매했을떄 총 금액이 가장 낮은 브랜드 반환
    fun getCheapestTotalPriceBrand(): GetCheapestTotalPriceBrandResult {
        val brands = brandReadComponent.loadAllBrands()
        val categories = this.getAllCategories()

        return GetCheapestTotalPriceBrandResult(
            // 모든 브랜드 순회하며 총합계 구하기 시작
            brands
                .associateWith { brand ->
                    categories
                        .associateWith { category ->
                            val brandCategorySummaryCache =
                                cheapestAndMostExpensiveProductPerBrandCacheComponent.getOrDoCache(brand.id!!, category.id)
                            CheapestProduct(
                                categoryId = brandCategorySummaryCache.categoryId,
                                categoryName = category.name,
                                brandId = brand.id,
                                brandName = brand.name,
                                cheapestProductId = brandCategorySummaryCache.cheapestProductId,
                                cheapestProductName = brandCategorySummaryCache.cheapestProductName,
                                cheapestProductMainPrice = brandCategorySummaryCache.cheapestProductMainPrice.toLong(),
                            )
                        }
                },
        )
    }

    // 특정 카테고리로 최저, 최고 브랜드와 가격 반환
    fun getCheapestAndMostExpensiveProductPerSpecificCategory(
        categoryName: String,
    ): GetSpecificCategoryCheapestAndMostExpensiveProductResult {
        val category = categoryReadComponent.getByNameOrNull(categoryName) ?: throw BadRequestException()
        val categorySummaryCache = cheapestAndExpensiveProductPerCategoryCacheComponent.getOrDoCache(category)
        val cheapest = cheapestAndMostExpensiveProductPerBrandCacheComponent.getOrDoCache(categorySummaryCache.cheapestBrandId, category.id)
        val mostExpensive =
            cheapestAndMostExpensiveProductPerBrandCacheComponent.getOrDoCache(
                categorySummaryCache.mostExpensiveBrandId,
                category.id,
            )

        return GetSpecificCategoryCheapestAndMostExpensiveProductResult(
            category = category,
            cheapestProduct =
                GetSpecificCategoryCheapestAndMostExpensiveProductResult.Product(
                    productId = cheapest.cheapestProductId,
                    brandId = cheapest.brandId,
                    brandName = cheapest.brandName,
                    mainPrice = cheapest.cheapestProductMainPrice,
                ),
            mostExpensiveProduct =
                GetSpecificCategoryCheapestAndMostExpensiveProductResult.Product(
                    productId = mostExpensive.mostExpensiveProductId,
                    brandId = mostExpensive.brandId,
                    brandName = mostExpensive.brandName,
                    mainPrice = mostExpensive.mostExpensiveProductMainPrice,
                ),
        )
    }
}

data class GetCheapestBrandPerCategoryResult(
    val cheapestProductPerCategoryMap: Map<Category, CheapestProduct>,
) {
    val totalPrice: Long =
        cheapestProductPerCategoryMap
            .values
            .sumOf { it.cheapestProductMainPrice }

    val asTsv =
        cheapestProductPerCategoryMap
            .map {
                "${it.key.name}\t${it.value.brandName}\t${NumberFormat.getInstance().format(it.value.cheapestProductMainPrice)}"
            }.joinToString(
                separator = "\n",
                prefix = "카테고리\t브랜드\t가격\n",
                postfix = "\n총액\t${NumberFormat.getInstance().format(totalPrice)}\n",
            )
}

data class CheapestProduct(
    val categoryId: Long,
    val categoryName: String,
    val brandId: Long,
    val brandName: String,
    val cheapestProductId: Long,
    val cheapestProductName: String,
    val cheapestProductMainPrice: Long,
)

data class GetCheapestTotalPriceBrandResult(
    val brandCheapestProductPerCategoryMap: Map<Brand, Map<Category, CheapestProduct>>,
) {
    val cheapestBrand =
        brandCheapestProductPerCategoryMap
            .minBy {
                it.value
                    .values
                    .sumOf { product -> product.cheapestProductMainPrice }
            }.key
}

data class GetSpecificCategoryCheapestAndMostExpensiveProductResult(
    val category: Category,
    val cheapestProduct: Product,
    val mostExpensiveProduct: Product,
) {
    data class Product(
        val productId: Long,
        val brandId: Long,
        val brandName: String,
        val mainPrice: Int,
    )
}
