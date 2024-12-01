package com.example.musinsa.api.controller

import com.example.musinsa.api.controller.dto.HomeWork1ResponseDTO
import com.example.musinsa.api.controller.dto.Homework2ResponseDTO
import com.example.musinsa.api.controller.dto.Homework3ResponseDTO
import com.example.musinsa.application.component.CategoryReadComponent
import com.example.musinsa.application.service.BrandCreateRequest
import com.example.musinsa.application.service.BrandService
import com.example.musinsa.application.service.CategoryService
import com.example.musinsa.application.service.ProductCreateRequest
import com.example.musinsa.application.service.ProductService
import jakarta.annotation.PostConstruct
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeworkController(
    private val categoryService: CategoryService,
    private val brandService: BrandService,
    private val productService: ProductService,
) {
    @PostConstruct
    fun init() {
        val header =
            arrayOf(
                CategoryReadComponent.CategoryType.상의,
                CategoryReadComponent.CategoryType.아우터,
                CategoryReadComponent.CategoryType.바지,
                CategoryReadComponent.CategoryType.스니커즈,
                CategoryReadComponent.CategoryType.가방,
                CategoryReadComponent.CategoryType.모자,
                CategoryReadComponent.CategoryType.양말,
                CategoryReadComponent.CategoryType.액세서리,
            )
        val data =
            arrayOf(
                arrayOf("A", "11200", "5500", "4200", "9000", "2000", "1700", "1800", "2300"),
                arrayOf("B", "10500", "5900", "3800", "9100", "2100", "2000", "2000", "2200"),
                arrayOf("C", "10000", "6200", "3300", "9200", "2200", "1900", "2200", "2100"),
                arrayOf("D", "10100", "5100", "3000", "9500", "2500", "1500", "2400", "2000"),
                arrayOf("E", "10700", "5000", "3800", "9900", "2300", "1800", "2100", "2100"),
                arrayOf("F", "11200", "7200", "4000", "9300", "2100", "1600", "2300", "1900"),
                arrayOf("G", "10500", "5800", "3900", "9000", "2200", "1700", "2100", "2000"),
                arrayOf("H", "10800", "6300", "3100", "9700", "2100", "1600", "2000", "2000"),
                arrayOf("I", "11400", "6700", "3200", "9500", "2400", "1700", "1700", "2400"),
            )

        data.forEach {
            val brand = brandService.create(BrandCreateRequest(it[0]))
            for (i in header.indices) {
                productService.create(
                    ProductCreateRequest(
                        brandId = brand.id,
                        categoryId = i + 1L,
                        name = "${it[i + 1]}_1",
                        mainPrice = it[i + 1].toInt(),
                    ),
                )
            }
        }
    }

    @GetMapping(value = ["/api/v1/home-work-1", "/api/v1/cheapest-brand-per-category"])
    fun getCheapestDealsByCategory(): HomeWork1ResponseDTO {
        return HomeWork1ResponseDTO.submit(categoryService.getCheapestBrandPerCategory())
    }

    @GetMapping(value = ["/api/v1/home-work-2", "/api/v1/cheapest-total-price-brand"])
    fun getCheapestTotalPriceBrand(): Homework2ResponseDTO {
        return Homework2ResponseDTO.submit(categoryService.getCheapestTotalPriceBrand())
    }

    @GetMapping(value = ["/api/v1/home-work-3", "/api/v1/cheapest-and-most-expensive-product-per-specific-category"])
    fun getCheapestAndMostExpensiveProductPerSpecificCategory(
        @RequestParam("category") category: String,
    ): Homework3ResponseDTO {
        return Homework3ResponseDTO.submit(categoryService.getCheapestAndMostExpensiveProductPerSpecificCategory(category))
    }
}
