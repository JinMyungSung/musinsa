package com.example.musinsa.api.controller.dto

import jakarta.validation.constraints.Size

data class BrandCreateRequestDTO(
    @field:Size(message = "브랜드명은 1자 이상, 100자 이하여야합니다.", min = 1, max = 100)
    val name: String?,
)
