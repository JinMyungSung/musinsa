package com.example.musinsa.api

import org.springframework.data.domain.Page

class ApiResponse<T>(
    val success: Boolean,
    val data: T?,
    val error: ErrorMessage? = null,
) {
    companion object {
        fun <T> ok(data: T?): ApiResponse<T> {
            return ApiResponse(
                success = true,
                data = data,
                error = null,
            )
        }

        fun <T> ok(pageResult: Page<T>): ApiResponse<PageResponse<T>> {
            return ApiResponse(
                success = true,
                data =
                    PageResponse(
                        items = pageResult.content,
                        totalCounts = pageResult.totalElements,
                        totalPages = pageResult.totalPages,
                        page = pageResult.pageable.pageNumber,
                        size = pageResult.pageable.pageSize,
                    ),
                error = null,
            )
        }

        fun <T> ok(
            items: List<T>,
            nextCursorExclusive: Long?,
        ): ApiResponse<CursorPageResponse<T>> {
            return ApiResponse(
                success = true,
                data =
                    CursorPageResponse(
                        items = items,
                        nextCursorExclusive = nextCursorExclusive,
                    ),
                error = null,
            )
        }

        fun fail(
            code: Int?,
            errorMessage: String?,
        ): ApiResponse<ErrorMessage> {
            return ApiResponse(
                success = false,
                data = null,
                error =
                    ErrorMessage(
                        code = code,
                        message = errorMessage,
                    ),
            )
        }
    }

    data class ErrorMessage(
        val code: Int?,
        val message: String?,
    )
}

class PageResponse<T>(
    val items: List<T>,
    val totalCounts: Long,
    val totalPages: Int,
    val page: Int,
    val size: Int,
)

class CursorPageResponse<T>(
    val items: List<T>,
    val nextCursorExclusive: Long?,
)
