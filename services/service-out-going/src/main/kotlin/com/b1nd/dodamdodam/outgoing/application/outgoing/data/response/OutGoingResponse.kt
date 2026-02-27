package com.b1nd.dodamdodam.outgoing.application.outgoing.data.response

import com.b1nd.dodamdodam.outgoing.domain.outgoing.entity.OutGoingEntity
import com.b1nd.dodamdodam.outgoing.domain.outgoing.enumeration.OutGoingStatusType
import java.time.LocalDateTime

data class StudentInfo(
    val id: Long,
    val name: String,
    val grade: Int,
    val room: Int,
    val number: Int
)

data class OutGoingResponse(
    val reason: String,
    val status: OutGoingStatusType,
    val student: StudentInfo,
    val rejectReason: String?,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime
) {
    companion object {
        fun fromEntity(entity: OutGoingEntity, student: StudentInfo): OutGoingResponse =
            OutGoingResponse(
                reason = entity.reason,
                status = entity.status,
                student = student,
                rejectReason = entity.rejectReason,
                startAt = entity.startAt,
                endAt = entity.endAt
            )
    }
}
