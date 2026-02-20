package com.b1nd.dodamdodam.outgoing.application.outgoing.data.response

import com.b1nd.dodamdodam.outgoing.domain.outgoing.entity.OutGoingEntity
import com.b1nd.dodamdodam.outgoing.domain.outgoing.enumeration.OutGoingStatus
import java.time.LocalDateTime

data class StudentInfo(
    val id: Long,
    val name: String,
    val grade: Int,
    val room: Int,
    val number: Int
)

data class OutGoingResponse(
    val id: Long,
    val reason: String,
    val status: OutGoingStatus,
    val student: StudentInfo,
    val rejectReason: String?,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val createdAt: LocalDateTime?,
    val modifiedAt: LocalDateTime?
) {
    companion object {
        fun of(entity: OutGoingEntity, student: StudentInfo): OutGoingResponse =
            OutGoingResponse(
                id = entity.id!!,
                reason = entity.reason,
                status = entity.status,
                student = student,
                rejectReason = entity.rejectReason,
                startAt = entity.startAt,
                endAt = entity.endAt,
                createdAt = entity.createdAt,
                modifiedAt = entity.modifiedAt
            )
    }
}
