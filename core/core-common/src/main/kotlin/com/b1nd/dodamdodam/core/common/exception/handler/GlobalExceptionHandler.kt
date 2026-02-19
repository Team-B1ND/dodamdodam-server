package com.b1nd.dodamdodam.core.common.exception.handler

import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.core.common.exception.BasicException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

interface GlobalExceptionHandler {
    fun handleDodamException(exception: BasicException): ResponseEntity<Response<Unit>>
}