package com.b1nd.dodamdodam.neis.domain.schedule.exception

import com.b1nd.dodamdodam.core.common.exception.ExceptionCode
import org.springframework.http.HttpStatus

enum class ScheduleExceptionCode(
    override val status: HttpStatus,
    override val message: String
) : ExceptionCode {
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "시간표 정보를 찾을 수 없어요."),
    COMCIGAN_API_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "컴시간 API 호출에 실패했어요."),
    COMCIGAN_SCHOOL_NOT_FOUND(HttpStatus.NOT_FOUND, "컴시간에서 학교를 찾을 수 없어요."),
    ;
}
