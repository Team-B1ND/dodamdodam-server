package com.b1nd.dodamdodam.notice.application.notice

import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.core.security.util.getCurrentUserId
import com.b1nd.dodamdodam.notice.application.notice.data.request.GenerateNoticeRequest
import com.b1nd.dodamdodam.notice.application.notice.data.request.ModifyNoticeRequest
import com.b1nd.dodamdodam.notice.application.notice.data.response.MemberInfoResponse
import com.b1nd.dodamdodam.notice.application.notice.data.response.NoticeResponse
import com.b1nd.dodamdodam.notice.application.notice.data.response.NoticeResponseMapper
import com.b1nd.dodamdodam.notice.domain.notice.entity.NoticeEntity
import com.b1nd.dodamdodam.notice.domain.notice.service.NoticeService
import com.b1nd.dodamdodam.notice.infrastructure.user.client.UserClient
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(rollbackFor = [Exception::class])
class NoticeUseCase(
    private val noticeService: NoticeService,
    private val userClient: UserClient,
    private val noticeResponseMapper: NoticeResponseMapper
) {
    fun register(request: GenerateNoticeRequest): Response<Long> {
        val userId = getCurrentUserId()
        val notice = noticeService.create(request.toEntity(userId))
        val files = request.toNoticeFiles(notice)
        if (files.isNotEmpty()) {
            noticeService.createFiles(files)
        }
        return Response.created("공지 생성 성공", notice.id)
    }

    @Transactional(readOnly = true)
    fun getNotices(keyword: String?, lastId: Long?, limit: Int): Response<List<NoticeResponse>> {
        val notices = noticeService.getAll(keyword, lastId, limit)
        val noticeResponses = convertToNoticeResponses(notices)
        return Response.ok("공지 목록 조회 성공", noticeResponses)
    }

    fun modify(id: Long, request: ModifyNoticeRequest): Response<Unit> {
        val userId = getCurrentUserId()
        noticeService.updateNotice(id, userId, request.title, request.content)
        return Response.ok("공지 수정 성공")
    }

    fun delete(id: Long): Response<Unit> {
        val userId = getCurrentUserId()
        noticeService.deleteNotice(id, userId)
        return Response.ok("공지 삭제 성공")
    }

    private fun convertToNoticeResponses(notices: List<NoticeEntity>): List<NoticeResponse> {
        val noticeFileMap = noticeService.getNoticeFileMap(notices)
        val userIds = notices.map { it.userId }.distinct()
        val memberInfoMap = fetchMemberInfos(userIds)

        return notices.map { notice ->
            noticeResponseMapper.toNoticeResponse(
                notice = notice,
                files = noticeFileMap.getOrDefault(notice.id!!, emptyList()),
                memberInfo = memberInfoMap[notice.userId]
            )
        }
    }

    private fun fetchMemberInfos(userIds: List<String>): Map<String, MemberInfoResponse> {
        if (userIds.isEmpty()) {
            return emptyMap()
        }
        return runBlocking {
            userClient.getUserInfosByUserIds(userIds)
        }
    }
}
