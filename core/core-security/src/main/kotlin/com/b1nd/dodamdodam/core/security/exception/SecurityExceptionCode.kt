package com.b1nd.dodamdodam.core.security.exception

import com.b1nd.dodamdodam.core.common.exception.ExceptionCode
import org.springframework.http.HttpStatus

enum class SecurityExceptionCode(
    override val status: HttpStatus,
    override val message: String
): ExceptionCode {
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없어요."),
    AUTHENTICATION_REQUIRED(HttpStatus.UNAUTHORIZED, "로그인이 필요해요."),
    USER_DISABLED(HttpStatus.FORBIDDEN, "사용자가 비활성 상태예요."),
}