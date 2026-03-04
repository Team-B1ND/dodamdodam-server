package com.b1nd.dodamdodam.club.infrastructure.exception

import com.b1nd.dodamdodam.core.common.exception.ExceptionCode
import org.springframework.http.HttpStatus

enum class ClubExceptionCode(
    override val status: HttpStatus,
    override val message: String
): ExceptionCode {
    CLUB_NOT_FOUNDED(HttpStatus.NOT_FOUND, "동아리가 존재하지 않아요."),
    CLUB_ALREADY_EXISTED(HttpStatus.CONFLICT, "이미 존재하는 동아리예요."),
    CLUB_NOT_OWNER(HttpStatus.FORBIDDEN, "동아리의 부장만 접근 가능해요."),
    CLUB_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 동아리의 멤버가 아니에요."),
    ;
}