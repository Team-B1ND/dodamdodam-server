package com.b1nd.dodamdodam.notice.application.notice.data.response

import com.b1nd.dodamdodam.notice.domain.notice.entity.NoticeEntity
import com.b1nd.dodamdodam.notice.domain.notice.entity.NoticeFileEntity
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
) {
    companion object {
        fun of(
            notice: NoticeEntity,
            files: List<NoticeFileEntity>,
            memberInfo: MemberInfoResponse?
        ): NoticeResponse {
            return NoticeResponse(
                id = notice.id!!,
                title = notice.title,
                content = notice.content,
                noticeStatus = notice.noticeStatus,
                noticeFileRes = NoticeFileResponse.of(files),
                memberInfoRes = memberInfo,
                createdAt = notice.createdAt,
                modifiedAt = notice.modifiedAt
            )
        }
    }
}
