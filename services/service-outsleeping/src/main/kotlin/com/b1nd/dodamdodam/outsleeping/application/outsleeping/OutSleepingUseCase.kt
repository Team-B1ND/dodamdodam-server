package com.b1nd.dodamdodam.outsleeping.application.outsleeping

import com.b1nd.dodamdodam.core.security.passport.PassportUserDetails
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.request.OutSleepingRequest
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.request.RejectRequest
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.response.OutSleepingResponse
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.response.ResidualStudentResponse
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.response.StudentInfo
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.entity.OutSleepingEntity
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.service.OutSleepingService
import com.b1nd.dodamdodam.outsleeping.infrastructure.user.client.UserGrpcClient
import jakarta.transaction.Transactional
import kotlinx.coroutines.runBlocking
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.UUID

@Component
@Transactional(rollbackOn = [Exception::class])
class OutSleepingUseCase(
    private val outSleepingService: OutSleepingService,
    private val userGrpcClient: UserGrpcClient
) {
    private fun currentUserId(): UUID {
        val principal = SecurityContextHolder.getContext().authentication?.principal
        return (principal as PassportUserDetails).passport.userId!!
    }

    fun apply(request: OutSleepingRequest) {
        val studentId = currentUserId()
        runBlocking { userGrpcClient.getStudentByUserId(studentId) }

        val entity = OutSleepingEntity(
            studentId = studentId,
            reason = request.reason,
            startAt = request.startAt,
            endAt = request.endAt
        )
        outSleepingService.create(entity)
    }

    fun allow(id: Long) = outSleepingService.allow(id)

    fun reject(id: Long, request: RejectRequest) =
        outSleepingService.reject(id, request.rejectReason)

    fun revert(id: Long) = outSleepingService.revert(id)

    fun delete(id: Long) = outSleepingService.delete(id, currentUserId())

    fun findByDate(date: LocalDate): List<OutSleepingResponse> {
        val entities = outSleepingService.findByDate(date)
        return enrichWithStudentInfo(entities)
    }

    fun findMy(): List<OutSleepingResponse> {
        val entities = outSleepingService.findByStudentId(currentUserId())
        return enrichWithStudentInfo(entities)
    }

    fun findResidualStudents(): List<ResidualStudentResponse> {
        val absentUserIds = outSleepingService.findValid(LocalDate.now()).map { it.studentId }
        val residualDtos = runBlocking { userGrpcClient.getResidualStudents(absentUserIds) }
        return residualDtos.map { ResidualStudentResponse.from(it) }
    }

    private fun enrichWithStudentInfo(entities: List<OutSleepingEntity>): List<OutSleepingResponse> {
        if (entities.isEmpty()) return emptyList()
        val userIds = entities.map { it.studentId }.distinct()
        val studentDtos = runBlocking { userGrpcClient.getStudentsByUserIds(userIds) }
        val studentByUserId = studentDtos.associateBy { UUID.fromString(it.userId) }

        return entities.mapNotNull { entity ->
            val dto = studentByUserId[entity.studentId] ?: return@mapNotNull null
            OutSleepingResponse.of(entity, StudentInfo(
                id = dto.studentId,
                name = dto.name,
                grade = dto.grade,
                room = dto.room,
                number = dto.number
            ))
        }
    }
}
