package com.b1nd.dodamdodam.inapp.domain.team.exception

import com.b1nd.dodamdodam.core.common.exception.ExceptionCode
import org.springframework.http.HttpStatus

enum class TeamExceptionCode(
    override val status: HttpStatus,
    override val message: String
): ExceptionCode {
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "팀을 찾을 수 없어요."),
    TEAM_NAME_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 팀 이름이예요.")
}