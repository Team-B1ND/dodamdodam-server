package com.b1nd.dodamdodam.outsleeping.application.data

import com.b1nd.dodamdodam.outsleeping.application.data.request.ApplyOutSleepingRequest
import com.b1nd.dodamdodam.outsleeping.application.data.response.OutSleepingResponse
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.entity.OutSleepingEntity
import java.util.UUID

fun OutSleepingEntity.toResponse(): OutSleepingResponse =
    OutSleepingResponse(
        id = id!!,
        userId = userId,
        reason = reason,
        status = status,
        rejectReason = rejectReason,
        startAt = startAt,
        endAt = endAt,
        createdAt = createdAt,
        modifiedAt = modifiedAt
    )

fun ApplyOutSleepingRequest.toEntity(userId: UUID): OutSleepingEntity =
    OutSleepingEntity(
        userId = userId,
        reason = reason,
        startAt = startAt,
        endAt = endAt
    )
