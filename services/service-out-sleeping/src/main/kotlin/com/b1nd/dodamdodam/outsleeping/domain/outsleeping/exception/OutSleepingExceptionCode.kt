package com.b1nd.dodamdodam.outsleeping.domain.outsleeping.exception

import com.b1nd.dodamdodam.core.common.exception.ExceptionCode
import org.springframework.http.HttpStatus

enum class OutSleepingExceptionCode(
    override val status: HttpStatus,
    override val message: String
) : ExceptionCode {
    OUT_SLEEPING_NOT_FOUND(HttpStatus.NOT_FOUND, "외박 신청을 찾을 수 없어요."),
    OUT_SLEEPING_NOT_OWNER(HttpStatus.FORBIDDEN, "본인의 외박 신청만 삭제할 수 있어요."),
    ;
}
