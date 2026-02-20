package com.b1nd.dodamdodam.notice.domain.notice.exception

import com.b1nd.dodamdodam.core.common.exception.ExceptionCode
import org.springframework.http.HttpStatus

enum class NoticeExceptionCode(
    override val status: HttpStatus,
    override val message: String
): ExceptionCode {
    NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND, "공지사항을 찾을 수 없어요."),
    NOT_NOTICE_AUTHOR(HttpStatus.FORBIDDEN, "공지사항 작성자가 아니에요."),
    INVALID_NOTICE_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 공지사항 요청이에요."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없어요."),
    USER_SERVICE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "사용자 서비스에 오류가 발생했어요."),
}
