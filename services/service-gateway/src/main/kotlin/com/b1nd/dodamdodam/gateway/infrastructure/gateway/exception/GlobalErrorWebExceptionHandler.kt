package com.b1nd.dodamdodam.gateway.infrastructure.gateway.exception

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.core.annotation.Order
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Order(-2)
class GlobalErrorWebExceptionHandler(
    private val objectMapper: ObjectMapper
) : ErrorWebExceptionHandler {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        val request = exchange.request
        val response = exchange.response

        log.error("Gateway error - Method: ${request.method}, Path: ${request.uri.path}, Error: ${ex.message}", ex)

        val status = when (ex) {
            is ResponseStatusException -> ex.statusCode
            else -> HttpStatus.INTERNAL_SERVER_ERROR
        }

        response.statusCode = status
        response.headers.contentType = MediaType.APPLICATION_JSON

        val errorResponse = mapOf(
            "status" to status.value(),
            "error" to status,
            "message" to (ex.message ?: "Unknown error"),
            "path" to request.uri.path
        )

        val buffer: DataBuffer = response.bufferFactory()
            .wrap(objectMapper.writeValueAsBytes(errorResponse))

        return response.writeWith(Mono.just(buffer))
    }
}
