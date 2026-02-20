package com.b1nd.dodamdodam.outsleeping.domain.outsleeping.exception

import com.b1nd.dodamdodam.core.common.exception.ExceptionCode
import org.springframework.http.HttpStatus

enum class OutSleepingExceptionCode(
    override val status: HttpStatus,
    override val message: String
) : ExceptionCode {
    OUT_SLEEPING_NOT_FOUND(HttpStatus.NOT_FOUND, "외박 신청을 찾을 수 없어요."),
    OUT_SLEEPING_FORBIDDEN(HttpStatus.FORBIDDEN, "외박 신청에 대한 권한이 없어요."),
    OUT_SLEEPING_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 해당 날짜에 외박 신청이 존재해요."),
    OUT_SLEEPING_NOT_PENDING(HttpStatus.BAD_REQUEST, "대기 중인 외박 신청만 삭제할 수 있어요."),
    OUT_SLEEPING_STUDENT_NOT_FOUND(HttpStatus.NOT_FOUND, "학생 정보를 찾을 수 없어요."),
}
