package com.b1nd.dodamdodam.inapp.domain.team.exception

import com.b1nd.dodamdodam.core.common.exception.ExceptionCode
import org.springframework.http.HttpStatus

enum class TeamExceptionCode(
    override val status: HttpStatus,
    override val message: String
): ExceptionCode {
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "팀을 찾을 수 없어요."),
    TEAM_NAME_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 팀 이름이예요."),
    TEAM_OWNER_PERMISSION_REQUIRED(HttpStatus.FORBIDDEN, "팀 오너 권한이 필요해요."),
    TEAM_CANNOT_REMOVE_OWNER(HttpStatus.FORBIDDEN, "팀 오너는 추방할 수 없어요."),
    TEAM_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "팀 멤버를 찾을 수 없어요."),
}