package com.example.musinsa.api.controller

import com.example.musinsa.application.service.ProductService
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.Rollback

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
class HomeworkControllerDeleteProductTest {
    @Autowired
    private lateinit var homeworkController: HomeworkController
    @Autowired
    private lateinit var productService: ProductService

    // 최저가부터 오름순으로 정렬된 상품을 삭제할 때 마다 최저가가 갱신되는지 확인하는 테스트입니다.
    @ParameterizedTest
    @CsvSource(
        value = [
            "C,10000",
            "D,10100",
            "G,10500",
            "B,10500",
            "E,10700",
            "H,10800",
            "F,11200",
            "A,11200",
            "I,11400",
        ]
    )
    @Rollback(value = false)
    fun `상의 카테고리의 상품을 금액이 낮은 순서대로 삭제하면 최저가가 갱신되어야한다`(brand: String, mainPrice: String) {
        val category = "상의"
        val result = homeworkController.getCheapestAndMostExpensiveProductPerSpecificCategory(category)
        result.최저가.shouldHaveSize(1)
        result.최저가[0].브랜드 shouldBe brand
        result.최저가[0].가격.replace(",", "") shouldBe mainPrice

        val brandNameProductViewMap = productService.search(0, 100)
            .content
            .filter { it.brandName == brand && it.categoryName == category }
            .associateBy { it.brandName }
        productService.delete(brandNameProductViewMap[brand]!!.productId!!)
    }

    // 최고가부터 내림순으로 정렬된 상품을 삭제할 때 마다 최고가가 갱신되는지 확인하는 테스트입니다.
    @ParameterizedTest
    @CsvSource(
        value = [
            "F,7200",
            "I,6700",
            "H,6300",
            "C,6200",
            "B,5900",
            "G,5800",
            "A,5500",
            "D,5100",
            "E,5000",
        ]
    )
    @Rollback(value = false)
    fun `아우터 카테고리의 상품을 금액이 높은 순서대로 삭제하면 최고가가 갱신되어야한다`(brand: String, mainPrice: String) {
        val category = "아우터"
        val result = homeworkController.getCheapestAndMostExpensiveProductPerSpecificCategory(category)
        result.최고가.shouldHaveSize(1)
        result.최고가[0].브랜드 shouldBe brand
        result.최고가[0].가격.replace(",", "") shouldBe mainPrice

        val brandNameProductViewMap = productService.search(0, 100)
            .content
            .filter { it.brandName == brand && it.categoryName == category }
            .associateBy { it.brandName }
        productService.delete(brandNameProductViewMap[brand]!!.productId!!)
    }
}
