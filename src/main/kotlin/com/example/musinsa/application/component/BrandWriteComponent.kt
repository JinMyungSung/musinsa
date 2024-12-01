package com.example.musinsa.application.component

import com.example.musinsa.application.domain.Brand
import com.example.musinsa.persistence.entity.BrandEntity
import com.example.musinsa.persistence.repository.BrandRepository
import org.springframework.stereotype.Component

@Component
class BrandWriteComponent(
    private val brandRepository: BrandRepository,
) {
    fun save(brand: Brand): Brand {
        return brandRepository.save(BrandEntity.fromDomain(brand)).toDomain()
    }
}
