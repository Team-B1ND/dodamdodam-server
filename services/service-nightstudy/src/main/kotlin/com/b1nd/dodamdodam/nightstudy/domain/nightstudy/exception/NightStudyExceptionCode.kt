package com.b1nd.dodamdodam.nightstudy.domain.nightstudy.exception

import com.b1nd.dodamdodam.core.common.exception.ExceptionCode
import org.springframework.http.HttpStatus

enum class NightStudyExceptionCode(
    override val status: HttpStatus,
    override val message: String
): ExceptionCode {
    NIGHT_STUDY_NOT_FOUND(HttpStatus.NOT_FOUND, "심야 자습 신청을 찾을 수 없어요."),
    NIGHT_STUDY_BANNED(HttpStatus.FORBIDDEN, "심야 자습이 정지된 인원이 있어요."),
    ;
}
