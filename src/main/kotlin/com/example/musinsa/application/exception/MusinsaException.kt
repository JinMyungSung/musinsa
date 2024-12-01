package com.example.musinsa.application.exception

enum class ExceptionType(val errorCode: Int, val defaultErrorMessage: String) {
    BAD_REQUEST(40000, "잘못된 요청입니다."),
    ALREADY_EXISTS_BRAND(40001, "이미 존재하는 브랜드명입니다."),
    LOCK_ACQUISITION_FAIL(40002, "잘못된 요청입니다."),
    NOT_FOUND_BRAND(40003, "존재하지 않는 브랜드입니다."),
    NOT_FOUND_CATEGORY(40004, "존재하지 않는 카테고리입니다."),
    NOT_FOUND_PRODUCT(40005, "존재하지 않는 상품입니다."),
    INTERNAL_SERVER_ERROR(50000, "잠시후 다시 시도해주세요."),
}

open class MusinsaException(
    open val code: Int?,
    open val errorMessage: String?,
) : Exception(errorMessage) {
    constructor(exceptionType: ExceptionType) : this(exceptionType.errorCode, exceptionType.defaultErrorMessage)
}

class BadRequestException : MusinsaException(ExceptionType.BAD_REQUEST)

class AlreadyExistsBrandException : MusinsaException(ExceptionType.ALREADY_EXISTS_BRAND)

class NotFoundBrandException : MusinsaException(ExceptionType.NOT_FOUND_BRAND)

class NotFoundCategoryException : MusinsaException(ExceptionType.NOT_FOUND_CATEGORY)

class NotFoundProductException : MusinsaException(ExceptionType.NOT_FOUND_PRODUCT)

class InternalServerError : MusinsaException(ExceptionType.INTERNAL_SERVER_ERROR)
