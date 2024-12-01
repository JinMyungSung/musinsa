package com.example.musinsa.application.domain

data class CheapestAndMostExpensiveProductPerBrandCacheV1(
    val brandId: Long,
    val brandName: String,
    val categoryId: Long,
    val categoryName: String,
    var cheapestProductId: Long,
    var cheapestProductName: String,
    var cheapestProductMainPrice: Int,
    var mostExpensiveProductId: Long,
    var mostExpensiveProductName: String,
    var mostExpensiveProductMainPrice: Int,
) {
    companion object {
        fun noProducts(
            brand: Brand,
            category: Category,
        ): CheapestAndMostExpensiveProductPerBrandCacheV1 {
            return CheapestAndMostExpensiveProductPerBrandCacheV1(
                brandId = brand.id!!,
                brandName = brand.name,
                categoryId = category.id,
                categoryName = category.name,
                cheapestProductId = 0L,
                cheapestProductName = "",
                cheapestProductMainPrice = Int.MAX_VALUE,
                mostExpensiveProductId = 0L,
                mostExpensiveProductName = "",
                mostExpensiveProductMainPrice = 0,
            )
        }
    }
}
