package com.b1nd.dodamdodam.nightstudy.domain.nightstudy.exception

import com.b1nd.dodamdodam.core.common.exception.ExceptionCode
import org.springframework.http.HttpStatus

enum class NightStudyExceptionCode(
    override val status: HttpStatus,
    override val message: String
) : ExceptionCode {
    NIGHT_STUDY_NOT_FOUND(HttpStatus.NOT_FOUND, "심야자습을 찾을 수 없어요."),
    NIGHT_STUDY_NOT_OWNER(HttpStatus.FORBIDDEN, "본인의 심야자습만 삭제할 수 있어요."),
    NIGHT_STUDY_BANNED(HttpStatus.FORBIDDEN, "심야자습이 정지된 상태에요."),
    ;
}
