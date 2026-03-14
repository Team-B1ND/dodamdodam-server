package com.b1nd.dodamdodam.outsleeping.application

import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.core.security.passport.holder.PassportHolder
import com.b1nd.dodamdodam.core.security.passport.requireUserId
import com.b1nd.dodamdodam.outsleeping.application.data.request.ApplyOutSleepingRequest
import com.b1nd.dodamdodam.outsleeping.application.data.request.RejectOutSleepingRequest
import com.b1nd.dodamdodam.outsleeping.application.data.response.OutSleepingResponse
import com.b1nd.dodamdodam.outsleeping.application.data.toEntity
import com.b1nd.dodamdodam.outsleeping.application.data.toResponse
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.exception.OutSleepingNotOwnerException
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.service.OutSleepingService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.UUID

@Component
@Transactional(rollbackFor = [Exception::class])
class OutSleepingUseCase(
    private val outSleepingService: OutSleepingService
) {

    fun apply(request: ApplyOutSleepingRequest): Response<Any> {
        val userId = PassportHolder.current().requireUserId()
        val entity = request.toEntity(userId)
        outSleepingService.save(entity)
        return Response.created("외박이 신청되었어요.")
    }

    fun allow(id: Long): Response<Any> {
        val outSleeping = outSleepingService.getById(id)
        outSleeping.allow()
        outSleepingService.save(outSleeping)
        return Response.ok("외박이 승인되었어요.")
    }

    fun reject(id: Long, request: RejectOutSleepingRequest): Response<Any> {
        val outSleeping = outSleepingService.getById(id)
        outSleeping.reject(request.rejectReason)
        outSleepingService.save(outSleeping)
        return Response.ok("외박이 거절되었어요.")
    }

    fun revert(id: Long): Response<Any> {
        val outSleeping = outSleepingService.getById(id)
        outSleeping.revert()
        outSleepingService.save(outSleeping)
        return Response.ok("외박이 대기 상태로 변경되었어요.")
    }

    fun delete(id: Long): Response<Any> {
        val userId = PassportHolder.current().requireUserId()
        val outSleeping = outSleepingService.getById(id)
        if (outSleeping.userId != userId) {
            throw OutSleepingNotOwnerException()
        }
        outSleeping.softDelete()
        outSleepingService.save(outSleeping)
        return Response.ok("외박이 취소되었어요.")
    }

    @Transactional(readOnly = true)
    fun getByDate(date: LocalDate): Response<List<OutSleepingResponse>> {
        val outSleepings = outSleepingService.getByDate(date).map { it.toResponse() }
        return Response.ok("외박을 조회했어요.", outSleepings)
    }

    @Transactional(readOnly = true)
    fun getMy(): Response<List<OutSleepingResponse>> {
        val userId = PassportHolder.current().requireUserId()
        val outSleepings = outSleepingService.getByUserId(userId).map { it.toResponse() }
        return Response.ok("내 외박을 조회했어요.", outSleepings)
    }

    @Transactional(readOnly = true)
    fun getResidual(date: LocalDate): Response<List<UUID>> {
        val allowedUserIds = outSleepingService.getAllowedByDate(date)
            .map { it.userId }
        return Response.ok("외박 승인된 학생을 조회했어요.", allowedUserIds)
    }
}
