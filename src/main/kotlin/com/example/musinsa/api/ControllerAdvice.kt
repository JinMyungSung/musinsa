package com.example.musinsa.api

import com.example.musinsa.application.exception.ExceptionType
import com.example.musinsa.application.exception.MusinsaException
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ControllerAdvice {
    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(value = [MusinsaException::class])
    fun musinsaException(e: MusinsaException): ResponseEntity<ApiResponse<ApiResponse.ErrorMessage>> {
        logger.error(e) { "도메인 예외발생" }
        return ResponseEntity.ok(ApiResponse.fail(e.code, e.errorMessage))
    }

    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun methodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ApiResponse<ApiResponse.ErrorMessage>> {
        logger.error(e) { "요청값 오류" }
        return ResponseEntity.ok(ApiResponse.fail(ExceptionType.BAD_REQUEST.errorCode, e.bindingResult.allErrors[0].defaultMessage))
    }

    @ExceptionHandler(value = [Throwable::class])
    fun throwable(e: Throwable): ResponseEntity<ApiResponse<ApiResponse.ErrorMessage>> {
        logger.error(e) { "미정의 예외발생" }
        return ResponseEntity.ok(
            ApiResponse.fail(ExceptionType.INTERNAL_SERVER_ERROR.errorCode, ExceptionType.INTERNAL_SERVER_ERROR.defaultErrorMessage),
        )
    }
}
