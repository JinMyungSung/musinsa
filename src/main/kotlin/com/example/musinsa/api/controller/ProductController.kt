package com.example.musinsa.api.controller

import com.example.musinsa.api.ApiResponse
import com.example.musinsa.api.PageResponse
import com.example.musinsa.api.controller.dto.ProductCreateRequestDTO
import com.example.musinsa.api.controller.dto.ProductCreateResponseDTO
import com.example.musinsa.api.controller.dto.ProductListViewResponseDTO
import com.example.musinsa.api.controller.dto.ProductUpdateRequestDTO
import com.example.musinsa.api.controller.dto.ProductUpdateResponseDTO
import com.example.musinsa.application.exception.BadRequestException
import com.example.musinsa.application.service.ProductCreateRequest
import com.example.musinsa.application.service.ProductService
import com.example.musinsa.application.service.ProductUpdateRequest
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductController(
    private val productService: ProductService,
) {
    @GetMapping("/api/v1/products")
    fun searchProducts(
        @RequestParam(required = false, defaultValue = "0") page: Int,
        @RequestParam(required = false, defaultValue = "20") size: Int,
    ): ApiResponse<PageResponse<ProductListViewResponseDTO>> {
        val result =
            productService.search(
                page = page,
                size = size,
            )

        return ApiResponse.ok(
            pageResult =
                result.map {
                    ProductListViewResponseDTO(
                        productId = it.productId,
                        brandId = it.brandId,
                        brandName = it.brandName,
                        categoryId = it.categoryId,
                        categoryName = it.categoryName,
                        name = it.name,
                        mainPrice = it.mainPrice,
                        deleteTs = it.deleteTs,
                        regTs = it.regTs,
                    )
                },
        )
    }

    @PostMapping("/api/v1/products")
    fun createProduct(
        @RequestBody @Valid request: ProductCreateRequestDTO,
    ): ApiResponse<ProductCreateResponseDTO> {
        val result =
            productService.create(
                request =
                    ProductCreateRequest(
                        brandId = request.brandId!!,
                        categoryId = request.categoryId!!,
                        name = request.name!!,
                        mainPrice = request.mainPrice!!,
                    ),
            )

        return ApiResponse.ok(
            ProductCreateResponseDTO(
                id = result.id,
            ),
        )
    }

    @PutMapping("/api/v1/products/{productId}")
    fun updateProduct(
        @PathVariable productId: Long,
        @RequestBody @Valid request: ProductUpdateRequestDTO,
    ): ApiResponse<ProductUpdateResponseDTO> {
        if (productId != request.id) {
            throw BadRequestException()
        }
        val result =
            productService.update(
                request =
                    ProductUpdateRequest(
                        id = request.id,
                        brandId = request.brandId!!,
                        categoryId = request.categoryId!!,
                        name = request.name!!,
                        mainPrice = request.mainPrice!!,
                    ),
            )
        return ApiResponse.ok(
            ProductUpdateResponseDTO(
                id = result.id,
            ),
        )
    }

    @DeleteMapping("/api/v1/products/{productId}")
    fun deleteProduct(
        @PathVariable productId: Long,
    ): ApiResponse<Unit> {
        productService.delete(productId)
        return ApiResponse.ok(Unit)
    }
}
