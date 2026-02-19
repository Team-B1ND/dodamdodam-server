package com.b1nd.dodamdodam.club.domain.club.exception

import com.b1nd.dodamdodam.core.common.exception.ExceptionCode
import org.springframework.http.HttpStatus

enum class ClubExceptionCode(
    override val status: HttpStatus,
    override val message: String
) : ExceptionCode {
    CLUB_NAME_DUPLICATE(HttpStatus.BAD_REQUEST, "이름이 중복됩니다."),
    CLUB_NOT_FOUND(HttpStatus.NOT_FOUND, "동아리를 찾을 수 없습니다."),
    CLUB_JOINED(HttpStatus.BAD_REQUEST, "이미 참여한 동아리입니다."),
    INVALID_CLUB_MEMBER_INVITATION(HttpStatus.BAD_REQUEST, "초대할 수 없는 학생입니다."),
    CLUB_PERMISSION_DENIED(HttpStatus.FORBIDDEN, "동아리 권한이 부족합니다."),
    INSUFFICIENT_CLUB_MEMBERS(HttpStatus.BAD_REQUEST, "동아리 최소 인원을 충족하지 않습니다."),
    CLUB_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없는 동아리 부원입니다."),
    APPLICATION_DURATION_PASSED(HttpStatus.FORBIDDEN, "동아리 신청 기간이 아닙니다."),
    OVERFLOW_MEMBER_SIZE(HttpStatus.BAD_REQUEST, "참여 가능 멤버 수를 넘었습니다."),
    ;
}
