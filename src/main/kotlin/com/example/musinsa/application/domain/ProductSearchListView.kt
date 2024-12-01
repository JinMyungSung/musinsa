package com.example.musinsa.application.domain

import java.time.LocalDateTime

data class ProductSearchListView(
    val productId: Long?,
    val brandId: Long,
    val brandName: String,
    val categoryId: Long,
    val name: String,
    val mainPrice: Int,
    val deleteTs: LocalDateTime?,
    val regTs: LocalDateTime,
    val updTs: LocalDateTime,
) {
    lateinit var categoryName: String
}
