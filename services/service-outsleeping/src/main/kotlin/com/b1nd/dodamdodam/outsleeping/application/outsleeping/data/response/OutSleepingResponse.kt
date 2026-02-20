package com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.response

import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.entity.OutSleepingEntity
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.enumeration.OutSleepingStatus
import java.time.LocalDate
import java.time.LocalDateTime

data class StudentInfo(
    val id: Long,
    val name: String,
    val grade: Int,
    val room: Int,
    val number: Int
)

data class OutSleepingResponse(
    val id: Long,
    val reason: String,
    val status: OutSleepingStatus,
    val student: StudentInfo,
    val rejectReason: String?,
    val startAt: LocalDate,
    val endAt: LocalDate,
    val createdAt: LocalDateTime?,
    val modifiedAt: LocalDateTime?
) {
    companion object {
        fun of(entity: OutSleepingEntity, student: StudentInfo): OutSleepingResponse =
            OutSleepingResponse(
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
