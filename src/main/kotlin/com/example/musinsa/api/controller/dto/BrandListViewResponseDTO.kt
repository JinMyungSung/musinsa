package com.example.musinsa.api.controller.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class BrandListViewResponseDTO(
    val id: Long,
    val name: String,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val deleteTs: LocalDateTime?,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val regTs: LocalDateTime,
)
