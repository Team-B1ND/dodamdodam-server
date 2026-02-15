package com.b1nd.dodamdodam.core.common.data

import com.b1nd.dodamdodam.core.common.exception.BasicException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class Response<T>(
    val status: Int,
    val message: String,
    val data: T? = null
) {
    companion object {
        fun <T> of(status: HttpStatus, message: String, data: T? = null) = Response(status.value(), message, data)
        fun <T> ok(message: String, data: T? = null) = of(HttpStatus.OK, message, data)
        fun <T> created(message: String, data: T? = null) = of(HttpStatus.CREATED, message, data)
        fun <T> noContent(message: String, data: T? = null) = of(HttpStatus.NO_CONTENT, message, data)
        fun error(exception: BasicException): Response<Unit> =
            of(exception.exceptionCode.status, exception.exceptionCode.message, null)
    }

    fun toResponseEntity(): ResponseEntity<Response<T>> =
        ResponseEntity
            .status(status)
            .body(this)
}