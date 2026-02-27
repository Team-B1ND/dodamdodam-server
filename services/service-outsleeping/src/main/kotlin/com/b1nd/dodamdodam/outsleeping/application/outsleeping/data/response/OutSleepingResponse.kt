package com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.response

import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.entity.OutSleepingEntity
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.enumeration.OutSleepingStatusType
import java.time.LocalDate

data class StudentInfo(
    val id: Long,
    val name: String,
    val grade: Int,
    val room: Int,
    val number: Int
)

data class OutSleepingResponse(
    val reason: String,
    val status: OutSleepingStatusType,
    val student: StudentInfo,
    val rejectReason: String?,
    val startAt: LocalDate,
    val endAt: LocalDate
) {
    companion object {
        fun fromEntity(entity: OutSleepingEntity, student: StudentInfo): OutSleepingResponse =
            OutSleepingResponse(
                reason = entity.reason,
                status = entity.status,
                student = student,
                rejectReason = entity.rejectReason,
                startAt = entity.startAt,
                endAt = entity.endAt
            )
    }
}
