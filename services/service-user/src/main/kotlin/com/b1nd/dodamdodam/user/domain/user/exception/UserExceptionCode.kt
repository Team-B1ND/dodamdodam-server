package com.b1nd.dodamdodam.user.domain.user.exception

import com.b1nd.dodamdodam.core.common.exception.ExceptionCode
import org.springframework.http.HttpStatus

enum class UserExceptionCode(
    override val status: HttpStatus,
    override val message: String
): ExceptionCode {
    USER_ALREADY_EXISTED(HttpStatus.CONFLICT, "이미 존재하는 유저예요."),
    USER_NOT_FOUNDED(HttpStatus.NOT_FOUND, "유저가 존재하지 않아요."),
    USER_PASSWORD_INCORRECT(HttpStatus.UNAUTHORIZED, "유저의 아이디 또는 비밀번호가 올바르지 않아요."),
    ;
}