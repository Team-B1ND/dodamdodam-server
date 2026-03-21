package com.b1nd.dodamdodam.outsleeping.application.outsleeping.data

import com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.request.ApplyOutSleepingRequest
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.response.DeadlineResponse
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.response.MemberResponse
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.response.OutSleepingResponse
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.response.StudentResponse
import com.b1nd.dodamdodam.outsleeping.domain.deadline.entity.OutSleepingDeadlineEntity
import com.b1nd.dodamdodam.outsleeping.domain.member.entity.MemberEntity
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.entity.OutSleepingEntity
import java.util.UUID

fun ApplyOutSleepingRequest.toEntity(userId: UUID) = OutSleepingEntity(
    userId = userId,
    reason = reason,
    startAt = startAt,
    endAt = endAt,
)

fun OutSleepingEntity.toResponse(member: MemberEntity?) = OutSleepingResponse(
    id = id!!,
    reason = reason,
    status = status,
    student = member?.toStudentResponse(),
    rejectReason = rejectReason,
    startAt = startAt,
    endAt = endAt,
    createdAt = createdAt,
    modifiedAt = modifiedAt,
)

fun List<OutSleepingEntity>.toResponses(member: MemberEntity?) =
    map { it.toResponse(member) }

fun List<OutSleepingEntity>.toResponses(memberMap: Map<UUID, MemberEntity>) =
    map { it.toResponse(memberMap[it.userId]) }

fun MemberEntity.toStudentResponse() = StudentResponse(
    id = id,
    name = name,
    grade = grade,
    room = room,
    number = number,
)

fun MemberEntity.toMemberResponse() = MemberResponse(
    id = userId,
    name = name,
    role = role,
    student = if (isStudent()) toStudentResponse() else null,
    createdAt = createdAt,
    modifiedAt = modifiedAt,
)

fun List<MemberEntity>.toMemberResponses() = map { it.toMemberResponse() }

fun OutSleepingDeadlineEntity.toResponse() = DeadlineResponse(
    dayOfWeek = dayOfWeek,
    time = time,
)
