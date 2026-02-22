package com.b1nd.dodamdodam.gateway.infrastructure.gateway.exception

import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.core.common.exception.BasicException
import com.b1nd.dodamdodam.core.common.exception.base.BaseInternalServerException
import com.fasterxml.jackson.databind.ObjectMapper
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
@Component
class GlobalErrorWebExceptionHandler(
    private val objectMapper: ObjectMapper
) : ErrorWebExceptionHandler {
    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        val response = exchange.response

        if (response.isCommitted) {
            return Mono.error(ex)
        }

        val body: Response<Unit> = when (ex) {
            is BasicException -> Response.error(ex)
            is ResponseStatusException -> Response.of(
                status = HttpStatus.valueOf(ex.statusCode.value()),
                message = ex.reason ?: "요청을 처리하지 못했어요."
            )
            else -> Response.error(BaseInternalServerException())
        }

        response.statusCode = HttpStatus.valueOf(body.status)
        response.headers.contentType = MediaType.APPLICATION_JSON

        val buffer: DataBuffer = response.bufferFactory()
            .wrap(objectMapper.writeValueAsBytes(body))

        return response.writeWith(Mono.just(buffer))
    }
}
