package com.b1nd.dodamdodam.club.infrastructure.exception

import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.core.common.exception.BasicException
import com.b1nd.dodamdodam.core.common.exception.handler.GlobalExceptionHandler
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ClubExceptionHandler: GlobalExceptionHandler {

    @ExceptionHandler(BasicException::class)
    override fun handleDodamException(exception: BasicException): ResponseEntity<Response<Unit>> {
        return Response.error(exception).toResponseEntity()
    }
}
