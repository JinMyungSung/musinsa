package com.example.musinsa.api.controller

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class HomeworkControllerTest {
    @Autowired
    private lateinit var homeworkController: HomeworkController

    @Test
    @Order(1)
    fun `과제 1 결과 테스트`() {
        val result = homeworkController.getCheapestDealsByCategory()
        result.tsv.shouldBe("카테고리\t브랜드\t가격\n상의\tC\t10,000\n아우터\tE\t5,000\n바지\tD\t3,000\n스니커즈\tG\t9,000\n가방\tA\t2,000\n모자\tD\t1,500\n양말\tI\t1,700\n액세서리\tF\t1,900\n총액\t34,100\n")
        result.totalPrice shouldBe "34,100"
    }

    @Test
    @Order(2)
    fun `과제 2 결과 테스트`() {
        val result = homeworkController.getCheapestTotalPriceBrand()
        result.최저가.브랜드 shouldBe "D"
        result.최저가.카테고리.shouldHaveSize(8)
        result.최저가.카테고리[0].카테고리 shouldBe "상의"
        result.최저가.카테고리[0].가격 shouldBe "10,100"
        result.최저가.카테고리[1].카테고리 shouldBe "아우터"
        result.최저가.카테고리[1].가격 shouldBe "5,100"
        result.최저가.카테고리[2].카테고리 shouldBe "바지"
        result.최저가.카테고리[2].가격 shouldBe "3,000"
        result.최저가.카테고리[3].카테고리 shouldBe "스니커즈"
        result.최저가.카테고리[3].가격 shouldBe "9,500"
        result.최저가.카테고리[4].카테고리 shouldBe "가방"
        result.최저가.카테고리[4].가격 shouldBe "2,500"
        result.최저가.카테고리[5].카테고리 shouldBe "모자"
        result.최저가.카테고리[5].가격 shouldBe "1,500"
        result.최저가.카테고리[6].카테고리 shouldBe "양말"
        result.최저가.카테고리[6].가격 shouldBe "2,400"
        result.최저가.카테고리[7].카테고리 shouldBe "액세서리"
        result.최저가.카테고리[7].가격 shouldBe "2,000"
        result.최저가.총액 shouldBe "36,100"
    }

    @Test
    @Order(3)
    fun `과제 3 결과 테스트`() {
        val result = homeworkController.getCheapestAndMostExpensiveProductPerSpecificCategory("상의")
        result.최저가.shouldHaveSize(1)
        result.최저가[0].브랜드 shouldBe "C"
        result.최저가[0].가격 shouldBe "10,000"

        result.최고가.shouldHaveSize(1)
        result.최고가[0].브랜드 shouldBe "I"
        result.최고가[0].가격 shouldBe "11,400"
    }
}
