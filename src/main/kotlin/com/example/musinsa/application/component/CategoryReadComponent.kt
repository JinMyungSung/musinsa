package com.example.musinsa.application.component

import com.example.musinsa.application.domain.Category
import com.example.musinsa.persistence.repository.ProductRepository
import org.springframework.stereotype.Component

@Component
class CategoryReadComponent(
    private val productRepository: ProductRepository,
) {
    fun hasProductCategories(): List<Category> {
        return productRepository.findCategoryIds()
            .mapNotNull { getByIdOrNull(it) }
    }

    fun getAllCategories(): List<Category> {
        return CategoryType.getAll()
            .map { Category(id = it.id, name = it.name) }
    }

    fun getByIdOrNull(id: Long): Category? {
        val found = CategoryType.getByIdOrNull(id) ?: return null
        return Category(id = found.id, name = found.name)
    }

    fun getByNameOrNull(name: String): Category? {
        val found = CategoryType.getByName(name) ?: return null
        return Category(id = found.id, name = found.name)
    }

    enum class CategoryType(val id: Long) {
        상의(1),
        아우터(2),
        바지(3),
        스니커즈(4),
        가방(5),
        모자(6),
        양말(7),
        액세서리(8),
        ;

        companion object {
            fun getAll(): List<CategoryType> {
                return entries
            }

            fun getByName(name: String): CategoryType? {
                val nameMap =
                    entries
                        .associateBy { it.name }
                return nameMap[name]
            }

            fun getByIdOrNull(id: Long): CategoryType? {
                return entries.firstOrNull { it.id == id }
            }
        }
    }
}
