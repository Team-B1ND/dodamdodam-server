package com.b1nd.dodamdodam.outgoing.application.outgoing

import com.b1nd.dodamdodam.core.security.passport.holder.PassportHolder
import com.b1nd.dodamdodam.core.security.passport.requireUserId
import com.b1nd.dodamdodam.outgoing.application.outgoing.data.request.OutGoingRequest
import com.b1nd.dodamdodam.outgoing.application.outgoing.data.request.RejectRequest
import com.b1nd.dodamdodam.outgoing.application.outgoing.data.response.OutGoingListResponse
import com.b1nd.dodamdodam.outgoing.application.outgoing.data.response.OutGoingResponse
import com.b1nd.dodamdodam.outgoing.application.outgoing.data.response.StudentInfo
import com.b1nd.dodamdodam.outgoing.domain.outgoing.entity.OutGoingEntity
import com.b1nd.dodamdodam.outgoing.domain.outgoing.service.OutGoingService
import com.b1nd.dodamdodam.outgoing.infrastructure.user.client.UserGrpcClient
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.UUID

@Component
@Transactional(rollbackFor = [Exception::class])
class OutGoingUseCase(
    private val outGoingService: OutGoingService,
    private val userGrpcClient: UserGrpcClient
) {

    fun apply(request: OutGoingRequest) {
        val studentId = PassportHolder.current().requireUserId()
        runBlocking { userGrpcClient.getStudentByUserId(studentId) }

        val entity = OutGoingEntity(
            studentId = studentId,
            reason = request.reason,
            startAt = request.startAt,
            endAt = request.endAt
        )
        outGoingService.create(entity)
    }

    fun allow(id: Long) = outGoingService.allow(id)

    fun reject(id: Long, request: RejectRequest) =
        outGoingService.reject(id, request.rejectReason)

    fun revert(id: Long) = outGoingService.revert(id)

    fun delete(id: Long) = outGoingService.delete(id, PassportHolder.current().requireUserId())

    @Transactional(readOnly = true)
    fun findByDate(date: LocalDate): OutGoingListResponse {
        val entities = outGoingService.findByDate(date)
        return OutGoingListResponse(enrichWithStudentInfo(entities))
    }

    @Transactional(readOnly = true)
    fun findMy(): OutGoingListResponse {
        val entities = outGoingService.findByStudentId(PassportHolder.current().requireUserId())
        return OutGoingListResponse(enrichWithStudentInfo(entities))
    }

    private fun enrichWithStudentInfo(entities: List<OutGoingEntity>): List<OutGoingResponse> {
        if (entities.isEmpty()) return emptyList()
        val userIds = entities.map { it.studentId }.distinct()
        val studentDtos = runBlocking { userGrpcClient.getStudentsByUserIds(userIds) }
        val studentByUserId = studentDtos.associateBy { UUID.fromString(it.userId) }

        return entities.mapNotNull { entity ->
            val dto = studentByUserId[entity.studentId] ?: return@mapNotNull null
            OutGoingResponse.fromEntity(entity, StudentInfo(
                id = dto.studentId,
                name = dto.name,
                grade = dto.grade,
                room = dto.room,
                number = dto.number
            ))
        }
    }
}
