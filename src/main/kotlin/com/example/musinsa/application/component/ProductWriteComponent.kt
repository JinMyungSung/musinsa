package com.example.musinsa.application.component

import com.example.musinsa.application.domain.Product
import com.example.musinsa.persistence.entity.ProductEntity
import com.example.musinsa.persistence.repository.ProductRepository
import org.springframework.stereotype.Component

@Component
class ProductWriteComponent(
    private val productRepository: ProductRepository,
) {
    fun save(product: Product): Product {
        return productRepository.save(ProductEntity.fromDomain(product)).toDomain()
    }
}
