package com.b1nd.dodamdodam.file.infrastructure.exception

import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.core.common.exception.BasicException
import com.b1nd.dodamdodam.core.common.exception.handler.GlobalExceptionHandler
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class FileExceptionHandler : GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(BasicException::class)
    override fun handleDodamException(exception: BasicException): ResponseEntity<Response<Unit>> {
        return Response.error(exception).toResponseEntity()
    }

    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception): ResponseEntity<Response<Unit>> {
        log.error("Unhandled exception", exception)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Response(status = 500, message = "서버 내부 오류가 발생했어요."))
    }
}
