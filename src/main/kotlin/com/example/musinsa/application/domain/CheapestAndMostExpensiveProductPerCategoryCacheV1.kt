package com.example.musinsa.application.domain

data class CheapestAndMostExpensiveProductPerCategoryCacheV1(
    val categoryId: Long,
    val categoryName: String,
    var cheapestBrandId: Long,
    var cheapestBrandName: String,
    var cheapestProductMainPrice: Int,
    var mostExpensiveBrandId: Long,
    var mostExpensiveBrandName: String,
    var mostExpensiveProductMainPrice: Int,
) {
    companion object {
        fun noProducts(
            categoryId: Long,
            categoryName: String,
        ): CheapestAndMostExpensiveProductPerCategoryCacheV1 {
            return CheapestAndMostExpensiveProductPerCategoryCacheV1(
                categoryId = categoryId,
                categoryName = categoryName,
                cheapestBrandId = 1L,
                cheapestBrandName = "",
                cheapestProductMainPrice = Int.MAX_VALUE,
                mostExpensiveBrandId = 0L,
                mostExpensiveBrandName = "",
                mostExpensiveProductMainPrice = 0,
            )
        }
    }
}
