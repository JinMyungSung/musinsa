package com.example.musinsa.application.component

import com.example.musinsa.application.domain.Brand
import com.example.musinsa.persistence.repository.BrandRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component
class BrandReadComponent(
    private val brandRepository: BrandRepository,
) {
    fun list(
        cursorExclusive: Long,
        size: Int,
    ): List<Brand> {
        return brandRepository.findByIdGreaterThanAndDeleteTsIsNullOrderByIdAsc(
            cursorExclusive = cursorExclusive,
            pageable = PageRequest.of(0, size),
        ).map {
            it.toDomain()
        }
    }

    fun loadAllBrands(): List<Brand> {
        var cursorExclusive: Long? = 0L
        val brands = mutableListOf<Brand>()
        do {
            val founds = list(cursorExclusive!!, 5) // 동작 검증을 위해 작은 size를 사용했습니다.
            brands.addAll(founds)
            cursorExclusive = founds.lastOrNull()?.id
        } while (cursorExclusive != null)

        return brands
    }

    fun findById(brandId: Long): Brand? {
        return brandRepository.findById(brandId)
            .getOrNull()
            ?.toDomain()
    }

    fun existsByName(name: String): Boolean {
        return findByName(name) != null
    }

    fun findByName(name: String): Brand? {
        return brandRepository.findByNameAndDeleteTsIsNull(name)?.toDomain()
    }
}
