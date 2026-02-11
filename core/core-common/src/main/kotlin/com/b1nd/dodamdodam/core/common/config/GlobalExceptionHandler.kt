package com.b1nd.dodamdodam.core.common.config

import com.b1nd.dodamdodam.core.common.exception.DodamException
import com.b1nd.dodamdodam.core.common.response.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(DodamException::class)
    fun handleDodamException(ex: DodamException): ResponseEntity<ApiResponse<Unit>> {
        val status = when (ex.code) {
            "BAD_REQUEST" -> HttpStatus.BAD_REQUEST
            "UNAUTHORIZED" -> HttpStatus.UNAUTHORIZED
            "FORBIDDEN" -> HttpStatus.FORBIDDEN
            "NOT_FOUND" -> HttpStatus.NOT_FOUND
            else -> HttpStatus.INTERNAL_SERVER_ERROR
        }
        return ResponseEntity.status(status)
            .body(ApiResponse.error(ex.message, ex.code))
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<ApiResponse<Unit>> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(ex.message ?: "Internal Server Error", "INTERNAL_SERVER_ERROR"))
    }
}
