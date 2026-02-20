package com.b1nd.dodamdodam.notice.application.notice

import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.core.security.passport.PassportUserDetails
import com.b1nd.dodamdodam.notice.application.notice.data.request.GenerateNoticeRequest
import com.b1nd.dodamdodam.notice.application.notice.data.request.ModifyNoticeRequest
import com.b1nd.dodamdodam.notice.application.notice.data.response.MemberInfoResponse
import com.b1nd.dodamdodam.notice.application.notice.data.response.NoticeResponse
import com.b1nd.dodamdodam.notice.domain.notice.entity.NoticeEntity
import com.b1nd.dodamdodam.notice.domain.notice.service.NoticeService
import com.b1nd.dodamdodam.notice.infrastructure.user.client.UserClient
import kotlinx.coroutines.runBlocking
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(rollbackFor = [Exception::class])
class NoticeUseCase(
    private val noticeService: NoticeService,
    private val userClient: UserClient
) {
    fun register(request: GenerateNoticeRequest): Response<Long> {
        val memberId = getCurrentUsername()
        val notice = noticeService.save(request.toEntity(memberId))
        val files = request.toNoticeFiles(notice)
        if (files.isNotEmpty()) {
            noticeService.saveAllFiles(files)
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
        val memberId = getCurrentUsername()
        noticeService.updateNotice(id, memberId, request.title, request.content)
        return Response.ok("공지 수정 성공")
    }

    fun delete(id: Long): Response<Unit> {
        val memberId = getCurrentUsername()
        noticeService.deleteNotice(id, memberId)
        return Response.ok("공지 삭제 성공")
    }

    private fun convertToNoticeResponses(notices: List<NoticeEntity>): List<NoticeResponse> {
        val noticeFileMap = noticeService.getNoticeFileMap(notices)
        val memberIds = notices.map { it.memberId }.distinct()
        val memberInfoMap = fetchMemberInfos(memberIds)

        return notices.map { notice ->
            NoticeResponse.of(
                notice = notice,
                files = noticeFileMap.getOrDefault(notice.id!!, emptyList()),
                memberInfo = memberInfoMap[notice.memberId]
            )
        }
    }

    private fun fetchMemberInfos(memberIds: List<String>): Map<String, MemberInfoResponse> {
        if (memberIds.isEmpty()) {
            return emptyMap()
        }
        return runBlocking {
            val result = mutableMapOf<String, MemberInfoResponse>()
            val infos = userClient.getUserInfosByUsernames(memberIds)
            memberIds.forEach { memberId ->
                infos[memberId]?.let { result[memberId] = it }
            }
            result
        }
    }

    private fun getCurrentUsername(): String {
        val userDetails = SecurityContextHolder.getContext().authentication.principal as PassportUserDetails
        return userDetails.username
    }
}
