package com.b1nd.dodamdodam.user.infrastructure.exception

import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.core.common.exception.BasicException
import com.b1nd.dodamdodam.core.common.exception.handler.GlobalExceptionHandler
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class UserExceptionHandler: GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(UserExceptionHandler::class.java)

    @ExceptionHandler(BasicException::class)
    override fun handleDodamException(exception: BasicException): ResponseEntity<Response<Unit>> {
        return Response.error(exception).toResponseEntity()
    }

    @ExceptionHandler(Exception::class)
    fun handleUnexpectedException(exception: Exception): ResponseEntity<Response<Unit>> {
        log.error("Unexpected exception", exception)
        return Response.of<Unit>(HttpStatus.INTERNAL_SERVER_ERROR, "서비스 요청을 처리하지 못했어요.")
            .toResponseEntity()
    }
}