package com.b1nd.dodamdodam.bus.domain.bus.exception

import com.b1nd.dodamdodam.core.common.exception.ExceptionCode
import org.springframework.http.HttpStatus

enum class BusExceptionCode(
    override val status: HttpStatus,
    override val message: String
): ExceptionCode {
    BUS_NOT_FOUND(HttpStatus.NOT_FOUND, "버스를 찾을 수 없어요."),
    BUS_APPLICANT_NOT_FOUND(HttpStatus.NOT_FOUND, "버스 신청자가 아니에요."),
    BUS_ALREADY_APPLIED_POSITION(HttpStatus.BAD_REQUEST, "이미 신청되어있는 자리예요."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없어요."),
    USER_SERVICE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "사용자 서비스에 오류가 발생했어요."),
}
