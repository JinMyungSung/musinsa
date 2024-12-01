package com.example.musinsa.persistence.repository

import com.example.musinsa.application.domain.ProductSearchListView
import com.example.musinsa.persistence.entity.ProductEntity
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProductRepository : JpaRepository<ProductEntity, Long> {
    fun findByIdAndDeleteTsIsNull(productId: Long): ProductEntity?

    @Query(
        """
        select new com.example.musinsa.application.domain.ProductSearchListView(
            p.id,
            p.brandId,
            b.name,
            p.categoryId,
            p.name,
            p.mainPrice,
            p.deleteTs,
            p.regTs,
            p.updTs
        )
        from ProductEntity p
        inner join BrandEntity b on p.brandId = b.id
        where p.deleteTs is null
        order by p.id desc
    """,
        nativeQuery = false,
    )
    fun search(pageable: Pageable): PageImpl<ProductSearchListView>

    @Query("select distinct p.categoryId from ProductEntity p", nativeQuery = false)
    fun findCategoryIds(): List<Long>

    fun findFirstByDeleteTsIsNullAndCategoryId(
        categoryId: Long,
        sort: Sort,
    ): ProductEntity?

    fun findFirstByDeleteTsIsNullAndBrandIdAndCategoryId(
        brandId: Long,
        categoryId: Long,
        sort: Sort,
    ): ProductEntity?
}
