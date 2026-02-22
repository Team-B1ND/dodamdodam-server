package com.b1nd.dodamdodam.core.common.exception.auth

import com.b1nd.dodamdodam.core.common.exception.ExceptionCode
import org.springframework.http.HttpStatus

enum class AuthTokenExceptionCode(
    override val status: HttpStatus,
    override val message: String
): ExceptionCode {
    INVALID_TOKEN_SIGNATURE(HttpStatus.UNAUTHORIZED, "올바르지 않은 토큰이에요."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "인증이 만료되었어요."),
}
