package com.b1nd.dodamdodam.core.common.exception

import org.springframework.http.HttpStatus

interface ExceptionCode {
    val status: HttpStatus
    val message: String
}