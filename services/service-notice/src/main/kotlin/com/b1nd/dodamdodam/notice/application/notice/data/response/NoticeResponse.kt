package com.b1nd.dodamdodam.notice.application.notice.data.response

import com.b1nd.dodamdodam.notice.domain.notice.enumeration.NoticeStatus
import java.time.LocalDateTime

data class NoticeResponse(
    val id: Long,
    val title: String,
    val content: String,
    val noticeStatus: NoticeStatus,
    val noticeFileRes: List<NoticeFileResponse>,
    val memberInfoRes: MemberInfoResponse?,
    val createdAt: LocalDateTime?,
    val modifiedAt: LocalDateTime?
)
