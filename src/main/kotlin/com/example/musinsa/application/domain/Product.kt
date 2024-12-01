package com.example.musinsa.application.domain

import java.time.LocalDateTime

data class Product(
    val id: Long?,
    val brandId: Long,
    var categoryId: Long,
    var name: String,
    var mainPrice: Int,
    var deleteTs: LocalDateTime?,
    val regTs: LocalDateTime,
    val updTs: LocalDateTime,
) {
    fun delete(deleteTs: LocalDateTime) {
        this.deleteTs = deleteTs
    }

    fun isDeleted(): Boolean {
        return deleteTs != null
    }

    fun update(
        categoryId: Long,
        name: String,
        mainPrice: Int,
    ) {
        this.categoryId = categoryId
        this.name = name
        this.mainPrice = mainPrice
    }

    companion object {
        fun createNew(
            brandId: Long,
            categoryId: Long,
            name: String,
            mainPrice: Int,
            deleteTs: LocalDateTime? = null,
            regTs: LocalDateTime = LocalDateTime.now(),
            updTs: LocalDateTime = LocalDateTime.now(),
        ): Product {
            return Product(
                id = null,
                brandId = brandId,
                categoryId = categoryId,
                name = name,
                mainPrice = mainPrice,
                deleteTs = deleteTs,
                regTs = regTs,
                updTs = updTs,
            )
        }

        fun create(
            id: Long?,
            brandId: Long,
            categoryId: Long,
            name: String,
            mainPrice: Int,
            deleteTs: LocalDateTime?,
            regTs: LocalDateTime,
            updTs: LocalDateTime,
        ): Product {
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
    }
}
