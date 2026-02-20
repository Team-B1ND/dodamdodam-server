package com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.exception

import com.b1nd.dodamdodam.core.common.exception.ExceptionCode
import org.springframework.http.HttpStatus

enum class WakeupSongExceptionCode(
    override val status: HttpStatus,
    override val message: String
) : ExceptionCode {
    WAKEUP_SONG_NOT_FOUND(HttpStatus.NOT_FOUND, "기상송을 찾을 수 없어요."),
    WAKEUP_SONG_ALREADY_APPLIED(HttpStatus.CONFLICT, "오늘 이미 기상송을 신청했어요."),
    WAKEUP_SONG_NOT_APPLICANT(HttpStatus.FORBIDDEN, "본인이 신청한 기상송이 아니에요."),
    WAKEUP_SONG_URL_MALFORMED(HttpStatus.BAD_REQUEST, "올바르지 않은 유튜브 URL이에요."),
    WAKEUP_SONG_UNSUPPORTED_TYPE(HttpStatus.BAD_REQUEST, "지원하지 않는 영상 타입이에요."),
    WAKEUP_SONG_SEARCH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "유튜브 검색에 실패했어요."),
    WAKEUP_SONG_STUDENT_NOT_FOUND(HttpStatus.NOT_FOUND, "학생 정보를 찾을 수 없어요."),
    ;
}
