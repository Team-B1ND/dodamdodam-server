package com.b1nd.dodamdodam.core.security.exception

import com.b1nd.dodamdodam.core.common.exception.ExceptionCode
import org.springframework.http.HttpStatus

enum class PassportExceptionCode(
    override val status: HttpStatus,
    override val message: String
): ExceptionCode {
    PASSPORT_EXPIRED(HttpStatus.UNAUTHORIZED, "인증 정보가 만료되었어요."),
    PASSPORT_INVALID(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 정보에요."),
    PASSPORT_DECOMPRESSION_FAILED(HttpStatus.BAD_REQUEST, "인증 데이터 압축 해제에 실패했어요.")
    ;
}