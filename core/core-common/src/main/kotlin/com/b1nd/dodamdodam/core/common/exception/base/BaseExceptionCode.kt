package com.b1nd.dodamdodam.core.common.exception.base

import com.b1nd.dodamdodam.core.common.exception.ExceptionCode
import org.springframework.http.HttpStatus

enum class BaseExceptionCode(
    override val status: HttpStatus,
    override val message: String
): ExceptionCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서비스 요청을 처리하지 못했어요."),
}