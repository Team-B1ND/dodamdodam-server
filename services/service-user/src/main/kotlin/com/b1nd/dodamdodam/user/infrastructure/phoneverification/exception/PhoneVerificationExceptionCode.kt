package com.b1nd.dodamdodam.user.infrastructure.phoneverification.exception

import com.b1nd.dodamdodam.core.common.exception.ExceptionCode
import org.springframework.http.HttpStatus

enum class PhoneVerificationExceptionCode(
    override val status: HttpStatus,
    override val message: String
): ExceptionCode {
    PHONE_VERIFICATION_CODE_EXPIRED(HttpStatus.BAD_REQUEST, "인증번호가 만료되었어요."),
    PHONE_VERIFICATION_CODE_MISMATCH(HttpStatus.BAD_REQUEST, "인증번호가 올바르지 않아요."),
    PHONE_NOT_VERIFIED(HttpStatus.FORBIDDEN, "휴대폰 인증이 완료되지 않았어요."),
}
