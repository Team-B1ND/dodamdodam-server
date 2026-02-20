package com.b1nd.dodamdodam.outgoing.domain.outgoing.exception

import com.b1nd.dodamdodam.core.common.exception.ExceptionCode
import org.springframework.http.HttpStatus

enum class OutGoingExceptionCode(
    override val status: HttpStatus,
    override val message: String
) : ExceptionCode {
    OUT_GOING_NOT_FOUND(HttpStatus.NOT_FOUND, "외출 신청을 찾을 수 없어요."),
    OUT_GOING_FORBIDDEN(HttpStatus.FORBIDDEN, "외출 신청에 대한 권한이 없어요."),
    OUT_GOING_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 해당 시간에 외출 신청이 존재해요."),
    OUT_GOING_NOT_PENDING(HttpStatus.BAD_REQUEST, "대기 중인 외출 신청만 삭제할 수 있어요."),
    OUT_GOING_STUDENT_NOT_FOUND(HttpStatus.NOT_FOUND, "학생 정보를 찾을 수 없어요."),
}
