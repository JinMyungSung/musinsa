package com.example.musinsa.persistence.repository

import com.example.musinsa.persistence.entity.BrandEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface BrandRepository : JpaRepository<BrandEntity, Long> {
    fun findByIdIn(ids: List<Long>): List<BrandEntity>

    fun findByNameAndDeleteTsIsNull(name: String): BrandEntity?

    fun findByIdGreaterThanAndDeleteTsIsNullOrderByIdAsc(
        cursorExclusive: Long,
        pageable: Pageable,
    ): List<BrandEntity>
}
