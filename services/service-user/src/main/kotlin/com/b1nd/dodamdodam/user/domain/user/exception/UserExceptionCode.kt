package com.b1nd.dodamdodam.user.domain.user.exception

import com.b1nd.dodamdodam.core.common.exception.ExceptionCode
import org.springframework.http.HttpStatus

enum class UserExceptionCode(
    override val status: HttpStatus,
    override val message: String
): ExceptionCode {
    USER_ALREADY_EXISTED(HttpStatus.CONFLICT, "이미 존재하는 유저에요.")
    ;
}