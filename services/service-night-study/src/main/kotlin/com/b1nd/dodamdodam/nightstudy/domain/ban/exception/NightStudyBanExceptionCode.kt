package com.b1nd.dodamdodam.nightstudy.domain.ban.exception

import com.b1nd.dodamdodam.core.common.exception.ExceptionCode
import org.springframework.http.HttpStatus

enum class NightStudyBanExceptionCode(
    override val status: HttpStatus,
    override val message: String
) : ExceptionCode {
    NIGHT_STUDY_BAN_NOT_FOUND(HttpStatus.NOT_FOUND, "심야자습 정지 정보를 찾을 수 없어요."),
    ;
}
