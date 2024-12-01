package com.example.musinsa.application.component

import com.example.musinsa.application.domain.Product
import com.example.musinsa.application.domain.ProductSearchListView
import com.example.musinsa.persistence.repository.ProductRepository
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Order
import org.springframework.stereotype.Component

@Component
class ProductReadComponent(
    private val productRepository: ProductRepository,
) {
    fun findById(productId: Long): Product? {
        return productRepository.findByIdAndDeleteTsIsNull(productId)?.toDomain()
    }

    fun search(pageable: Pageable): PageImpl<ProductSearchListView> {
        return productRepository.search(
            pageable = pageable,
        )
    }

    fun findFirstByCategoryIdAndUpdTsGteOrderByMainPrice(
        categoryId: Long,
        ascending: Boolean,
    ): Product? {
        val order =
            if (ascending) {
                listOf(Order.asc("mainPrice"), Order.desc("id"))
            } else {
                listOf(Order.desc("mainPrice"), Order.desc("id"))
            }
        return productRepository.findFirstByDeleteTsIsNullAndCategoryId(
            categoryId = categoryId,
            sort = Sort.by(order),
        )?.toDomain()
    }

    fun findFirstByBrandIdAndCategoryIdAndUpdTsGteOrderByMainPrice(
        brandId: Long,
        categoryId: Long,
        ascending: Boolean,
    ): Product? {
        val order =
            if (ascending) {
                listOf(Order.asc("mainPrice"), Order.desc("id"))
            } else {
                listOf(Order.desc("mainPrice"), Order.desc("id"))
            }
        return productRepository.findFirstByDeleteTsIsNullAndBrandIdAndCategoryId(
            brandId = brandId,
            categoryId = categoryId,
            sort = Sort.by(order),
        )?.toDomain()
    }
}
