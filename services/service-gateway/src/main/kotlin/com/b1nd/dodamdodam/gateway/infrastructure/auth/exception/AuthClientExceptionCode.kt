package com.b1nd.dodamdodam.gateway.infrastructure.auth.exception

import com.b1nd.dodamdodam.core.common.exception.ExceptionCode
import org.springframework.http.HttpStatus

enum class AuthClientExceptionCode(
    override val status: HttpStatus,
    override val message: String
): ExceptionCode {
    PASSPORT_EXCHANGE_FAILED(HttpStatus.FORBIDDEN, "접근 권한 발급에 실패했어요."),
}