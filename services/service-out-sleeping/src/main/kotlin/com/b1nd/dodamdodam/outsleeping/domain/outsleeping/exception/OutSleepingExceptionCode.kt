package com.b1nd.dodamdodam.outsleeping.domain.outsleeping.exception

import com.b1nd.dodamdodam.core.common.exception.ExceptionCode
import org.springframework.http.HttpStatus

enum class OutSleepingExceptionCode(
    override val status: HttpStatus,
    override val message: String
) : ExceptionCode {
    OUT_SLEEPING_NOT_FOUND(HttpStatus.NOT_FOUND, "외박 신청을 찾을 수 없어요."),
    OUT_SLEEPING_NOT_OWNER(HttpStatus.FORBIDDEN, "본인의 외박 신청만 수정하거나 취소할 수 있어요."),
    OUT_SLEEPING_ALREADY_PROCESSED(HttpStatus.BAD_REQUEST, "이미 처리된 외박 신청이에요."),
    OUT_SLEEPING_DEADLINE_EXCEEDED(HttpStatus.BAD_REQUEST, "외박 신청 기간이 지났어요."),
    ;
}
