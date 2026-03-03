package com.b1nd.dodamdodam.core.common.data

import com.b1nd.dodamdodam.core.common.exception.BasicException
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import java.io.FilterInputStream
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

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

        fun toFileResponseEntity(file: Path, filename: String, contentType: String): ResponseEntity<Resource> {
            val fileSize = Files.size(file)
            val encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8)
                .replace("+", "%20")

            val cleanupStream = object : FilterInputStream(Files.newInputStream(file)) {
                override fun close() {
                    super.close()
                    Files.deleteIfExists(file)
                }
            }

            val resource = object : InputStreamResource(cleanupStream) {
                override fun contentLength(): Long = fileSize
            }

            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''$encodedFilename")
                .contentType(MediaType.parseMediaType(contentType))
                .contentLength(fileSize)
                .body(resource)
        }
    }

    fun toResponseEntity(): ResponseEntity<Response<T>> =
        ResponseEntity
            .status(status)
            .body(this)
}