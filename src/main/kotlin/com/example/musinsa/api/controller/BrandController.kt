package com.example.musinsa.api.controller

import com.example.musinsa.api.ApiResponse
import com.example.musinsa.api.CursorPageResponse
import com.example.musinsa.api.controller.dto.BrandCreateRequestDTO
import com.example.musinsa.api.controller.dto.BrandCreateResponseDTO
import com.example.musinsa.api.controller.dto.BrandListViewResponseDTO
import com.example.musinsa.api.controller.dto.BrandUpdateRequestDTO
import com.example.musinsa.api.controller.dto.BrandUpdateResponseDTO
import com.example.musinsa.application.exception.BadRequestException
import com.example.musinsa.application.service.BrandCreateRequest
import com.example.musinsa.application.service.BrandService
import com.example.musinsa.application.service.BrandUpdateRequest
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
class BrandController(
    private val brandService: BrandService,
) {
    @GetMapping("/api/v1/brands")
    fun listBrands(
        @RequestParam(required = false, defaultValue = "0") cursorExclusive: Long?,
        @RequestParam(required = false, defaultValue = "20") size: Int,
    ): ApiResponse<CursorPageResponse<BrandListViewResponseDTO>> {
        val result =
            brandService.list(
                cursorExclusive = cursorExclusive,
                size = size,
            )

        return ApiResponse.ok(
            items =
                result.map {
                    BrandListViewResponseDTO(
                        id = it.id!!,
                        name = it.name,
                        deleteTs = it.deleteTs,
                        regTs = it.regTs,
                    )
                },
            nextCursorExclusive = result.lastOrNull()?.id,
        )
    }

    @PostMapping("/api/v1/brands")
    fun createBrand(
        @RequestBody @Valid request: BrandCreateRequestDTO,
    ): ApiResponse<BrandCreateResponseDTO> {
        val result =
            brandService.create(
                request =
                    BrandCreateRequest(
                        name = request.name!!,
                    ),
            )

        return ApiResponse.ok(
            BrandCreateResponseDTO(
                id = result.id,
            ),
        )
    }

    @PutMapping("/api/v1/brands/{brandId}")
    fun updateBrand(
        @PathVariable brandId: Long,
        @RequestBody @Valid request: BrandUpdateRequestDTO,
    ): ApiResponse<BrandUpdateResponseDTO> {
        if (brandId != request.id) {
            throw BadRequestException()
        }
        val result =
            brandService.update(
                request =
                    BrandUpdateRequest(
                        id = request.id,
                        name = request.name!!,
                    ),
            )
        return ApiResponse.ok(
            BrandUpdateResponseDTO(
                id = result.id,
            ),
        )
    }

    @DeleteMapping("/api/v1/brands/{brandId}")
    fun deleteBrand(
        @PathVariable brandId: Long,
    ): ApiResponse<Unit> {
        brandService.delete(brandId)
        return ApiResponse.ok(Unit)
    }
}
