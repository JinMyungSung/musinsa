package com.example.musinsa.application.service

import com.example.musinsa.application.domain.Category
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback

@SpringBootTest
@Rollback
class ProductServiceTest {
    @Autowired
    private lateinit var productService: ProductService

    @Autowired
    private lateinit var categoryService: CategoryService

    @Test
    fun `상품을 조회할 수 있다`() {
        val products = productService.search(0, 100)
        products.size shouldBeGreaterThan 72
    }

    @Test
    fun `천원짜리 상의를 등록하면 최저가 상의가 변경된다`() {
        val found = categoryService.getCheapestBrandPerCategory().cheapestProductPerCategoryMap[Category(1L, "상의")]
        found.shouldNotBeNull()
        found.cheapestProductMainPrice shouldBe 10000

        val newProduct =
            productService.create(
                ProductCreateRequest(
                    brandId = 1L,
                    categoryId = 1L,
                    name = "테스트상의등록",
                    mainPrice = 1000,
                ),
            )

        newProduct.id.shouldNotBeNull()
        val found2 = categoryService.getCheapestBrandPerCategory().cheapestProductPerCategoryMap[Category(1L, "상의")]
        found2.shouldNotBeNull()
        found2.cheapestProductId shouldBe newProduct.id
        found2.cheapestProductMainPrice shouldBe 1000
    }

    @Test
    fun `구천원짜리 스니커즈의 가격을 변경해도 최저가 스니커즈는 다른 셀러의 9000원이다`() {
        val targetCategory = Category(4L, "스니커즈")
        val found = categoryService.getCheapestBrandPerCategory().cheapestProductPerCategoryMap[targetCategory]
        found.shouldNotBeNull()
        found.cheapestProductMainPrice shouldBe 9000

        val updated =
            productService.update(
                ProductUpdateRequest(
                    id = found.cheapestProductId,
                    brandId = found.brandId,
                    categoryId = found.categoryId,
                    name = found.cheapestProductName,
                    mainPrice = 10000,
                ),
            )

        updated.id.shouldNotBeNull()
        val found2 = categoryService.getCheapestBrandPerCategory().cheapestProductPerCategoryMap[targetCategory]
        found2.shouldNotBeNull()
        found2.cheapestProductId shouldNotBe updated.id
        found2.cheapestProductMainPrice shouldBe 9000
    }

    @Test
    fun `오천원짜리 아우터를 삭제하면 최저가 아우터의 가격은 5100원으로 변경된다`() {
        val targetCategory = Category(2L, "아우터")
        val found = categoryService.getCheapestBrandPerCategory().cheapestProductPerCategoryMap[targetCategory]
        found.shouldNotBeNull()
        found.cheapestProductMainPrice shouldBe 5000

        productService.delete(found.cheapestProductId)

        val found2 = categoryService.getCheapestBrandPerCategory().cheapestProductPerCategoryMap[targetCategory]
        found2.shouldNotBeNull()
        found2.cheapestProductId shouldNotBe found.cheapestProductId
        found2.cheapestProductMainPrice shouldBe 5100
    }
}
