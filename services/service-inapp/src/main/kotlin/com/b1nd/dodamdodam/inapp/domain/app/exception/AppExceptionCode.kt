package com.b1nd.dodamdodam.inapp.domain.app.exception

import com.b1nd.dodamdodam.core.common.exception.ExceptionCode
import org.springframework.http.HttpStatus

enum class AppExceptionCode(
    override val status: HttpStatus,
    override val message: String
): ExceptionCode {
    APP_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 앱이예요."),
    APP_NOT_FOUND(HttpStatus.NOT_FOUND, "앱을 찾을 수 없어요."),
    APP_RELEASE_NOT_FOUND(HttpStatus.NOT_FOUND, "릴리즈를 찾을 수 없어요."),
    APP_RELEASE_ENABLE_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "ALLOWED 상태의 릴리즈만 활성화할 수 있어요."),
    APP_TEAM_MEMBER_PERMISSION_REQUIRED(HttpStatus.FORBIDDEN, "앱을 관리할 팀원 권한이 필요해요."),
    APP_TEAM_OWNER_PERMISSION_REQUIRED(HttpStatus.FORBIDDEN, "앱을 관리할 팀 오너 권한이 필요해요."),
    ;
}
