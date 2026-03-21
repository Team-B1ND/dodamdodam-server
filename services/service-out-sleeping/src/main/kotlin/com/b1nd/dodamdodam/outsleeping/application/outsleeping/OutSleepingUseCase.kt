package com.b1nd.dodamdodam.outsleeping.application.outsleeping

import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.core.security.passport.holder.PassportHolder
import com.b1nd.dodamdodam.core.security.passport.requireUserId
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.request.ApplyOutSleepingRequest
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.request.DenyOutSleepingRequest
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.request.ModifyOutSleepingRequest
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.request.UpdateDeadlineRequest
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.response.DeadlineResponse
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.response.MemberResponse
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.response.OutSleepingResponse
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.response.PageResponse
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.toEntity
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.toMemberResponses
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.toResponse
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.toResponses
import com.b1nd.dodamdodam.outsleeping.domain.deadline.service.OutSleepingDeadlineService
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.service.OutSleepingService
import com.b1nd.dodamdodam.outsleeping.infrastructure.grpc.client.UserClient
import kotlinx.coroutines.runBlocking
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.UUID

@Component
@Transactional(rollbackFor = [Exception::class])
class OutSleepingUseCase(
    private val outSleepingService: OutSleepingService,
    private val userClient: UserClient,
    private val deadlineService: OutSleepingDeadlineService,
) {

    fun apply(request: ApplyOutSleepingRequest): Response<Any> {
        deadlineService.validateDeadline()
        outSleepingService.create(request.toEntity(currentUserId()))
        return Response.created("외박 신청이 완료되었어요.")
    }

    fun modify(id: Long, request: ModifyOutSleepingRequest): Response<Any> {
        deadlineService.validateDeadline()
        val userId = currentUserId()
        val outSleeping = outSleepingService.getById(id)
        outSleepingService.validateOwner(outSleeping, userId)
        outSleeping.update(request.reason, request.startAt, request.endAt)
        return Response.ok("외박 신청이 수정되었어요.")
    }

    fun cancel(id: Long): Response<Any> {
        val userId = currentUserId()
        val outSleeping = outSleepingService.getById(id)
        outSleepingService.validateOwner(outSleeping, userId)
        outSleepingService.validatePending(outSleeping)
        outSleepingService.delete(outSleeping)
        return Response.ok("외박 신청이 취소되었어요.")
    }

    @Transactional(readOnly = true)
    fun getMy(): Response<List<OutSleepingResponse>> {
        val userId = currentUserId()
        val outSleepings = outSleepingService.getByUserId(userId)
        val userInfo = runBlocking { userClient.getUserInfo(userId) }
        val userInfoMap = mapOf(userId to userInfo)
        return Response.ok("내 외박 신청 목록을 조회했어요.", outSleepings.toResponses(userInfoMap))
    }

    @Transactional(readOnly = true)
    fun getByDate(date: LocalDate, page: Int, size: Int): Response<PageResponse<OutSleepingResponse>> {
        val pageable = PageRequest.of(page, size)
        val outSleepings = outSleepingService.getByDate(date, pageable)
        val userInfoMap = getUserInfoMap(outSleepings.content.map { it.userId })
        val responses = outSleepings.content.toResponses(userInfoMap)
        return Response.ok(
            "외박 신청 목록을 조회했어요.",
            PageResponse(
                content = responses,
                page = outSleepings.number,
                size = outSleepings.size,
                totalElements = outSleepings.totalElements,
                totalPages = outSleepings.totalPages,
            )
        )
    }

    @Transactional(readOnly = true)
    fun getValid(): Response<List<OutSleepingResponse>> {
        val outSleepings = outSleepingService.getAllowedByDate(LocalDate.now())
        val userInfoMap = getUserInfoMap(outSleepings.map { it.userId })
        return Response.ok("유효한 외박 목록을 조회했어요.", outSleepings.toResponses(userInfoMap))
    }

    @Transactional(readOnly = true)
    fun getResidual(): Response<List<MemberResponse>> {
        val allowedUserIds = outSleepingService.getAllowedByDate(LocalDate.now())
            .map { it.userId }.toSet()
        val allStudents = runBlocking { userClient.getAllStudents() }
        val residualStudents = allStudents.filter {
            UUID.fromString(it.publicId) !in allowedUserIds
        }
        return Response.ok("잔류 학생을 조회했어요.", residualStudents.toMemberResponses())
    }

    fun allow(id: Long): Response<Any> {
        val outSleeping = outSleepingService.getById(id)
        outSleeping.allow()
        return Response.ok("외박 신청을 승인했어요.")
    }

    fun deny(id: Long, request: DenyOutSleepingRequest): Response<Any> {
        val outSleeping = outSleepingService.getById(id)
        outSleeping.deny(request.denyReason)
        return Response.ok("외박 신청을 거절했어요.")
    }

    fun revert(id: Long): Response<Any> {
        val outSleeping = outSleepingService.getById(id)
        outSleeping.revert()
        return Response.ok("외박 신청 상태를 되돌렸어요.")
    }

    @Transactional(readOnly = true)
    fun getDeadline(): Response<List<DeadlineResponse>> {
        val deadlines = deadlineService.getAll()
        return Response.ok("외박 신청 마감 시간을 조회했어요.", deadlines.map { it.toResponse() })
    }

    fun updateDeadline(request: UpdateDeadlineRequest): Response<Any> {
        deadlineService.update(request.dayOfWeek, request.time)
        return Response.ok("외박 신청 마감 시간이 수정되었어요.")
    }

    private fun getUserInfoMap(userIds: List<UUID>) = runBlocking {
        userClient.getUserInfosByIds(userIds)
    }.associateBy { UUID.fromString(it.publicId) }

    private fun currentUserId() = PassportHolder.current().requireUserId()
}
