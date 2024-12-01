package com.example.musinsa.api.controller.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class ProductListViewResponseDTO(
    val productId: Long?,
    val brandId: Long,
    val brandName: String,
    val categoryId: Long,
    val categoryName: String,
    val name: String,
    val mainPrice: Int,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val deleteTs: LocalDateTime?,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val regTs: LocalDateTime,
)
