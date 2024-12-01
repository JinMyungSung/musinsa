package com.example.musinsa.api.controller.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class ProductCreateRequestDTO(
    @field:NotNull(message = "브랜드를 선택해주세요.")
    val brandId: Long?,
    @field:NotNull(message = "카테고리를 선택해주세요.")
    val categoryId: Long?,
    @field:Size(message = "상품명은 1자 이상, 100자 이하여야합니다.", min = 1, max = 100)
    val name: String?,
    @field:NotNull(message = "대표 가격은 입력해주세요.")
    @field:Min(message = "대표 가격은 0원 이상이어야합니다.", value = 1)
    @field:Max(message = "대표 가격은 2,147,483,647원 이상이어야합니다.", value = Int.MAX_VALUE.toLong())
    val mainPrice: Int?,
)
