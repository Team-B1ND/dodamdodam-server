package com.b1nd.dodamdodam.auth.domain.principal.exception

import com.b1nd.dodamdodam.core.common.exception.ExceptionCode
import org.springframework.http.HttpStatus

enum class PrincipalExceptionStatusCode(
    override val status: HttpStatus,
    override val message: String
): ExceptionCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없어요."),
    PASSWORD_INCORRECT(HttpStatus.UNAUTHORIZED, "비밀번호가 올바르지 않아요."),
}