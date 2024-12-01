package com.example.musinsa.application.domain

import java.time.LocalDateTime

data class Brand(
    val id: Long?,
    var name: String,
    var deleteTs: LocalDateTime?,
    val regTs: LocalDateTime,
    val updTs: LocalDateTime,
) {
    fun isDeleted(): Boolean {
        return deleteTs != null
    }

    fun delete(deleteTs: LocalDateTime) {
        this.deleteTs = deleteTs
    }

    fun update(name: String) {
        this.name = name
    }

    companion object {
        fun createNew(
            name: String,
            deleteTs: LocalDateTime? = null,
            regTs: LocalDateTime = LocalDateTime.now(),
            updTs: LocalDateTime = LocalDateTime.now(),
        ): Brand {
            return Brand(
                id = null,
                name = name,
                deleteTs = deleteTs,
                regTs = regTs,
                updTs = updTs,
            )
        }

        fun create(
            id: Long,
            name: String,
            deleteTs: LocalDateTime?,
            regTs: LocalDateTime,
            updTs: LocalDateTime,
        ): Brand {
            return Brand(
                id = id,
                name = name,
                deleteTs = deleteTs,
                regTs = regTs,
                updTs = updTs,
            )
        }
    }
}
