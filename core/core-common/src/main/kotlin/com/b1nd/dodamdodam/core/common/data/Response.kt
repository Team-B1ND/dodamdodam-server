package com.b1nd.dodamdodam.core.common.data

import com.b1nd.dodamdodam.core.common.exception.BasicException
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class Response<T>(
    val status: Int,
    val message: String,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val data: T? = null,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val code: String? = null
) {
    companion object {
        fun <T> of(status: HttpStatus, message: String, data: T? = null, code: String? = null) = Response(status.value(), message, data, code)
        fun <T> ok(message: String, data: T? = null) = of(HttpStatus.OK, message, data)
        fun <T> created(message: String, data: T? = null) = of(HttpStatus.CREATED, message, data)
        fun <T> noContent(message: String, data: T? = null) = of(HttpStatus.NO_CONTENT, message, data)
        fun error(exception: BasicException): Response<Unit> =
            of(exception.exceptionCode.status, exception.exceptionCode.message, null, exception.exceptionCode.code)

        fun toFileResponseEntity(response: FileDownloadResponse): ResponseEntity<Resource> {
            val disposition = ContentDisposition.attachment()
                .filename(response.filename, Charsets.UTF_8)
                .build()

            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .contentType(response.contentType)
                .contentLength(response.bytes.size.toLong())
                .body(ByteArrayResource(response.bytes))
        }
    }

    fun toResponseEntity(): ResponseEntity<Response<T>> =
        ResponseEntity
            .status(status)
            .body(this)
}