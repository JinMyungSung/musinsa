package com.example.musinsa.api.controller

import com.example.musinsa.api.ApiResponse
import com.example.musinsa.api.controller.dto.CategoryListViewResponseDTO
import com.example.musinsa.application.service.CategoryService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CategoryController(
    private val categoryService: CategoryService,
) {
    @GetMapping("/api/v1/categories")
    fun getAllCategories(): ApiResponse<List<CategoryListViewResponseDTO>> {
        return ApiResponse.ok(
            categoryService.getAllCategories()
                .map {
                    CategoryListViewResponseDTO(
                        id = it.id,
                        name = it.name,
                    )
                },
        )
    }
}
