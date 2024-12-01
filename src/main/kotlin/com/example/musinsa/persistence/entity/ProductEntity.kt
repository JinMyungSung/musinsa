package com.example.musinsa.persistence.entity

import com.example.musinsa.application.domain.Product
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@Entity
@Table(
    name = "product",
    indexes = [
        Index(name = "idx_brand_id", columnList = "brandId", unique = false),
        Index(name = "idx_category_id_brand_id", columnList = "categoryId, brandId", unique = false),
    ],
)
class ProductEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long?,
    @Column(name = "brandId")
    val brandId: Long,
    @Column(name = "categoryId")
    val categoryId: Long,
    @Column(name = "name", nullable = false)
    val name: String,
    @Column(name = "mainPrice")
    val mainPrice: Int,
    @Column(name = "deleteTs", nullable = true, columnDefinition = "datetime(0) null default null")
    val deleteTs: LocalDateTime?,
    @Column(name = "regTs", columnDefinition = "datetime(0) null default null")
    val regTs: LocalDateTime,
    @LastModifiedDate
    @Column(name = "updTs", columnDefinition = "datetime(0) null default null")
    val updTs: LocalDateTime,
) {
    fun toDomain(): Product {
        return Product(
            id = id,
            brandId = brandId,
            categoryId = categoryId,
            name = name,
            mainPrice = mainPrice,
            deleteTs = deleteTs,
            regTs = regTs,
            updTs = updTs,
        )
    }

    companion object {
        fun fromDomain(product: Product): ProductEntity {
            return ProductEntity(
                id = product.id,
                brandId = product.brandId,
                categoryId = product.categoryId,
                name = product.name,
                mainPrice = product.mainPrice,
                deleteTs = product.deleteTs,
                regTs = product.regTs,
                updTs = product.updTs,
            )
        }
    }
}
