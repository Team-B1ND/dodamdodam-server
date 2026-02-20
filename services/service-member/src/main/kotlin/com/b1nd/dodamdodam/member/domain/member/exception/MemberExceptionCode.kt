package com.b1nd.dodamdodam.member.domain.member.exception

import com.b1nd.dodamdodam.core.common.exception.ExceptionCode
import org.springframework.http.HttpStatus

enum class MemberExceptionCode(
    override val status: HttpStatus,
    override val message: String
) : ExceptionCode {
    MEMBER_ALREADY_ACTIVE(HttpStatus.BAD_REQUEST, "이미 활성화된 회원입니다"),
    INVALID_AUTH_CODE(HttpStatus.BAD_REQUEST, "유효하지 않은 인증코드입니다"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "유효하지 않은 요청 형식입니다"),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "유효하지 않은 이메일 형식입니다"),
    INVALID_PHONE(HttpStatus.BAD_REQUEST, "유효하지 않은 전화번호 형식입니다"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다"),
    AUTH_CODE_EXPIRED(HttpStatus.UNAUTHORIZED, "인증코드가 만료되었습니다"),
    MEMBER_DEACTIVATED(HttpStatus.FORBIDDEN, "비활성화된 회원입니다"),
    PERMISSION_DENIED(HttpStatus.FORBIDDEN, "해당 작업을 수행할 권한이 없습니다"),
    MEMBER_PENDING(HttpStatus.FORBIDDEN, "대기 중인 회원입니다"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다"),
    BROADCAST_CLUB_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "방송부원을 찾을 수 없습니다"),
    DORMITORY_MANAGE_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "자치위원을 찾을 수 없습니다"),
    STUDENT_NOT_FOUND(HttpStatus.NOT_FOUND, "학생 정보를 찾을 수 없습니다"),
    TEACHER_NOT_FOUND(HttpStatus.NOT_FOUND, "교사 정보를 찾을 수 없습니다"),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다"),
    DUPLICATE_BROADCAST_CLUB_MEMBER(HttpStatus.CONFLICT, "이미 등록된 방송부원입니다"),
    DUPLICATE_DORMITORY_MANAGE_MEMBER(HttpStatus.CONFLICT, "이미 등록된 자치위원입니다"),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다"),
    MEMBER_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "회원 처리 중 오류가 발생했습니다"),
    AUTH_CODE_SEND_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "인증코드 발송 중 오류가 발생했습니다"),
}
