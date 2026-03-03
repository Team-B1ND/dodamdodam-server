package com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.exception

import com.b1nd.dodamdodam.core.common.exception.ExceptionCode
import org.springframework.http.HttpStatus

enum class WakeupSongExceptionCode(
    override val status: HttpStatus,
    override val message: String
) : ExceptionCode {
    WAKEUP_SONG_NOT_FOUND(HttpStatus.NOT_FOUND, "기상송을 찾을 수 없어요."),
    WAKEUP_SONG_NOT_OWNER(HttpStatus.FORBIDDEN, "본인의 기상송만 삭제할 수 있어요."),
    YOUTUBE_VIDEO_NOT_FOUND(HttpStatus.NOT_FOUND, "유튜브 영상을 찾을 수 없어요."),
    MELON_CHART_FETCH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "멜론 차트를 가져올 수 없어요."),
    AUDIO_DOWNLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "오디오 다운로드에 실패했어요."),
    ;
}
