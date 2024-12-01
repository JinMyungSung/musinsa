package com.example.musinsa.persistence.entity

import com.example.musinsa.application.domain.Brand

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@Entity
@Table(name = "brand")
class BrandEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long?,
    @Column(name = "name", nullable = false)
    val name: String,
    @Column(name = "deleteTs", nullable = true, columnDefinition = "datetime(0) null default null")
    val deleteTs: LocalDateTime?,
    @Column(name = "regTs", columnDefinition = "datetime(0) null default null")
    val regTs: LocalDateTime,
    @LastModifiedDate
    @Column(name = "updTs", columnDefinition = "datetime(0) null default null")
    val updTs: LocalDateTime,
) {
    fun toDomain(): Brand {
        return Brand(
            id = id,
            name = name,
            deleteTs = deleteTs,
            regTs = regTs,
            updTs = updTs,
        )
    }

    companion object {
        fun fromDomain(brand: Brand): BrandEntity {
            return BrandEntity(
                id = brand.id,
                name = brand.name,
                deleteTs = brand.deleteTs,
                regTs = brand.regTs,
                updTs = brand.updTs,
            )
        }
    }
}
