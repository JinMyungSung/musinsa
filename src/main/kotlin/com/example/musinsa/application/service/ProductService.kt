package com.example.musinsa.application.service

import com.example.musinsa.application.component.BrandReadComponent
import com.example.musinsa.application.component.CategoryReadComponent
import com.example.musinsa.application.component.CheapestAndExpensiveProductPerCategoryCacheComponent
import com.example.musinsa.application.component.CheapestAndMostExpensiveProductPerBrandCacheComponent
import com.example.musinsa.application.component.ProductReadComponent
import com.example.musinsa.application.component.ProductWriteComponent
import com.example.musinsa.application.domain.Brand
import com.example.musinsa.application.domain.Category
import com.example.musinsa.application.domain.CheapestAndMostExpensiveProductPerBrandCacheV1
import com.example.musinsa.application.domain.Product
import com.example.musinsa.application.domain.ProductSearchListView
import com.example.musinsa.application.exception.NotFoundBrandException
import com.example.musinsa.application.exception.NotFoundCategoryException
import com.example.musinsa.application.exception.NotFoundProductException
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ProductService(
    private val categoryReadComponent: CategoryReadComponent,
    private val brandReadComponent: BrandReadComponent,
    private val productReadComponent: ProductReadComponent,
    private val productWriteComponent: ProductWriteComponent,
    private val cheapestAndMostExpensiveProductPerBrandCacheComponent: CheapestAndMostExpensiveProductPerBrandCacheComponent,
    private val cheapestAndExpensiveProductPerCategoryCacheComponent: CheapestAndExpensiveProductPerCategoryCacheComponent,
) {
    fun search(
        page: Int,
        size: Int,
    ): PageImpl<ProductSearchListView> {
        val categoryIdMap =
            categoryReadComponent
                .getAllCategories()
                .associateBy { it.id }

        val result = productReadComponent.search(PageRequest.of(page, size))
        result.forEach { it.categoryName = categoryIdMap[it.categoryId]!!.name }
        return result
    }

    fun create(request: ProductCreateRequest): ProductCreateResult {
        val brand = brandReadComponent.findById(request.brandId) ?: throw NotFoundBrandException()
        val category = categoryReadComponent.getByIdOrNull(request.categoryId) ?: throw NotFoundCategoryException()
        val newProduct =
            Product.createNew(
                brandId = brand.id!!,
                categoryId = category.id,
                name = request.name,
                mainPrice = request.mainPrice,
            )

        val savedProduct = productWriteComponent.save(newProduct)
        syncCategoryCache(product = savedProduct, brand = brand, category = category)

        return ProductCreateResult(id = savedProduct.id!!)
    }

    private fun syncCategoryCache(
        product: Product,
        brand: Brand,
        category: Category,
    ) {
        // 상품
        val brandCategoryCache = syncBrandCategoryCache(product, category)
        val categorySummaryCache = cheapestAndExpensiveProductPerCategoryCacheComponent.getOrDoCache(category)
        var needSyncCache = false
        if (categorySummaryCache.cheapestProductMainPrice > brandCategoryCache.cheapestProductMainPrice) {
            categorySummaryCache.cheapestBrandId = brandCategoryCache.brandId
            categorySummaryCache.cheapestBrandName = brand.name
            categorySummaryCache.cheapestProductMainPrice = brandCategoryCache.cheapestProductMainPrice
            needSyncCache = true
        }

        if (categorySummaryCache.mostExpensiveProductMainPrice < brandCategoryCache.mostExpensiveProductMainPrice) {
            categorySummaryCache.mostExpensiveBrandId = brandCategoryCache.brandId
            categorySummaryCache.mostExpensiveBrandName = brand.name
            categorySummaryCache.mostExpensiveProductMainPrice = brandCategoryCache.mostExpensiveProductMainPrice
            needSyncCache = true
        }

        if (needSyncCache) {
            cheapestAndExpensiveProductPerCategoryCacheComponent.setCache(category.id, categorySummaryCache)
        }
    }

    private fun syncBrandCategoryCache(
        product: Product,
        category: Category,
    ): CheapestAndMostExpensiveProductPerBrandCacheV1 {
        val cache = cheapestAndMostExpensiveProductPerBrandCacheComponent.getOrDoCache(product.brandId, category.id)

        // 브랜드 카테고리 캐시에서 바라보는 상품이 이 상품이거나, 캐시된 금액보다 상품의 가격이 크거나 작으면 갱신해야한다.
        if (cache.cheapestProductId == product.id!! ||
            cache.mostExpensiveProductId == product.id ||
            cache.cheapestProductMainPrice > product.mainPrice ||
            cache.mostExpensiveProductMainPrice < product.mainPrice
        ) {
            cheapestAndMostExpensiveProductPerBrandCacheComponent.syncBrandCategorySummaryCacheFromDB(
                brandId = product.brandId,
                categoryId = category.id,
            )
        }

        return cache
    }

    fun update(request: ProductUpdateRequest): ProductUpdateResult {
        val product = productReadComponent.findById(request.id) ?: throw NotFoundProductException()
        val brand = brandReadComponent.findById(request.brandId) ?: throw NotFoundBrandException()
        val category = categoryReadComponent.getByIdOrNull(request.categoryId) ?: throw NotFoundCategoryException()
        val beforeCategory = categoryReadComponent.getByIdOrNull(request.categoryId) ?: throw NotFoundCategoryException()
        val categoryChanged = beforeCategory.id != request.categoryId

        // 상품 업데이트
        product.update(
            categoryId = request.categoryId,
            name = request.name,
            mainPrice = request.mainPrice,
        )
        // 디비 저장
        val savedProduct = productWriteComponent.save(product)
        // 상품의 카테고리 ID가 변경됐고, 이 상품이 캐시에서 바라보던 상품이면 캐시를 갱신한다.
        if (categoryChanged) {
            syncCategoryCache(product = product, brand = brand, category = beforeCategory)
        }
        // 상품의 현재 카테고리 서머리 캐시를 갱신한다.
        syncCategoryCache(product = savedProduct, brand = brand, category = category)

        return ProductUpdateResult(id = savedProduct.id!!)
    }

    @Transactional(readOnly = false)
    fun delete(
        productId: Long,
        deleteTs: LocalDateTime = LocalDateTime.now(),
    ) {
        val product = productReadComponent.findById(productId) ?: throw NotFoundProductException()
        val brand = brandReadComponent.findById(product.brandId) ?: throw NotFoundBrandException()
        val category = categoryReadComponent.getByIdOrNull(product.categoryId) ?: throw NotFoundCategoryException()
        if (product.isDeleted()) {
            throw NotFoundProductException()
        }
        product.delete(deleteTs)
        productWriteComponent.save(product)
        syncCategoryCache(product = product, brand = brand, category = category)
    }
}

data class ProductCreateRequest(
    val brandId: Long,
    val categoryId: Long,
    val name: String,
    val mainPrice: Int,
)

data class ProductCreateResult(
    val id: Long,
)

data class ProductUpdateRequest(
    val id: Long,
    val brandId: Long,
    val categoryId: Long,
    val name: String,
    val mainPrice: Int,
)

data class ProductUpdateResult(
    val id: Long,
)
