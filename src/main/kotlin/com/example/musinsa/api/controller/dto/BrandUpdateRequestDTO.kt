package com.example.musinsa.api.controller.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class BrandUpdateRequestDTO(
    @field:NotNull(message = "수정할 브랜드를 선택해주세요.")
    val id: Long?,
    @field:NotNull(message = "브랜드명을 입력해주세요.")
    @field:Size(message = "브랜드명은 1자 이상, 100자 이하여야합니다.", min = 1, max = 100)
    val name: String?,
)
