package com.b1nd.dodamdodam.notice.application.notice.data.response

import com.b1nd.dodamdodam.notice.domain.notice.entity.NoticeEntity
import com.b1nd.dodamdodam.notice.domain.notice.entity.NoticeFileEntity
import org.springframework.stereotype.Component

@Component
class NoticeResponseMapper {

    fun toNoticeResponse(
        notice: NoticeEntity,
        files: List<NoticeFileEntity>,
        memberInfo: MemberInfoResponse?
    ): NoticeResponse {
        return NoticeResponse(
            id = notice.id!!,
            title = notice.title,
            content = notice.content,
            noticeStatus = notice.noticeStatus,
            noticeFileRes = NoticeFileResponse.fromNoticeFileEntities(files),
            memberInfoRes = memberInfo,
            createdAt = notice.createdAt,
            modifiedAt = notice.modifiedAt
        )
    }
}
