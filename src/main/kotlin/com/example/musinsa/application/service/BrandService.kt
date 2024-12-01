package com.example.musinsa.application.service

import com.example.musinsa.application.annotation.RedisLock
import com.example.musinsa.application.component.BrandReadComponent
import com.example.musinsa.application.component.BrandWriteComponent
import com.example.musinsa.application.domain.Brand
import com.example.musinsa.application.exception.AlreadyExistsBrandException
import com.example.musinsa.application.exception.NotFoundBrandException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class BrandService(
    private val brandReadComponent: BrandReadComponent,
    private val brandWriteComponent: BrandWriteComponent,
) {
    fun list(
        cursorExclusive: Long?,
        size: Int,
    ): List<Brand> {
        return brandReadComponent.list(
            cursorExclusive = cursorExclusive ?: 0L,
            size = size,
        )
    }

    @RedisLock(lockKey = "${LOCK_BRAND_NAME_V1_KEY}:#request.name")
    @Transactional(readOnly = false)
    fun create(request: BrandCreateRequest): BrandCreateResult {
        if (brandReadComponent.existsByName(request.name)) {
            throw AlreadyExistsBrandException()
        }
        val updated = brandWriteComponent.save(Brand.createNew(name = request.name))
        return BrandCreateResult(id = updated.id!!)
    }

    @RedisLock(lockKey = "${LOCK_BRAND_NAME_V1_KEY}:#request.name")
    @Transactional(readOnly = false)
    fun update(request: BrandUpdateRequest): BrandUpdateResult {
        val brand = brandReadComponent.findById(request.id) ?: throw NotFoundBrandException()
        if (brand.name == request.name) {
            return BrandUpdateResult(id = brand.id!!)
        }
        if (brandReadComponent.existsByName(request.name)) {
            throw AlreadyExistsBrandException()
        }
        brand.update(name = request.name)
        val result = brandWriteComponent.save(brand)
        return BrandUpdateResult(id = result.id!!)
    }

    @Transactional(readOnly = false)
    fun delete(
        id: Long,
        deleteTs: LocalDateTime = LocalDateTime.now(),
    ) {
        val brand = brandReadComponent.findById(id) ?: throw NotFoundBrandException()
        if (brand.isDeleted()) {
            throw NotFoundBrandException()
        }
        brand.delete(deleteTs)
        brandWriteComponent.save(brand)
    }

    companion object {
        const val LOCK_BRAND_NAME_V1_KEY = "LOCK_BRAND_NAME_V1"
    }
}

data class BrandCreateRequest(
    val name: String,
)

data class BrandCreateResult(
    val id: Long,
)

data class BrandUpdateRequest(
    val id: Long,
    val name: String,
)

data class BrandUpdateResult(
    val id: Long,
)
