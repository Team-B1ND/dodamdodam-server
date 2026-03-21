package com.b1nd.dodamdodam.outsleeping.application.outsleeping.data

import com.b1nd.dodamdodam.grpc.user.UserInfoMessage
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.request.ApplyOutSleepingRequest
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.response.DeadlineResponse
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.response.MemberResponse
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.response.OutSleepingResponse
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.response.StudentResponse
import com.b1nd.dodamdodam.outsleeping.domain.deadline.entity.OutSleepingDeadlineEntity
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.entity.OutSleepingEntity
import java.util.UUID

fun ApplyOutSleepingRequest.toEntity(userId: UUID) = OutSleepingEntity(
    userId = userId,
    reason = reason,
    startAt = startAt,
    endAt = endAt,
)

fun OutSleepingEntity.toResponse(userInfo: UserInfoMessage?) = OutSleepingResponse(
    id = id!!,
    reason = reason,
    status = status,
    student = userInfo?.toStudentResponse(),
    denyReason = denyReason,
    startAt = startAt,
    endAt = endAt,
    createdAt = createdAt,
    modifiedAt = modifiedAt,
)

fun List<OutSleepingEntity>.toResponses(userInfoMap: Map<UUID, UserInfoMessage>) =
    map { it.toResponse(userInfoMap[it.userId]) }

fun UserInfoMessage.toStudentResponse() = StudentResponse(
    name = name,
    grade = if (hasGrade()) grade else null,
    room = if (hasRoom()) room else null,
    number = if (hasNumber()) number else null,
)

fun UserInfoMessage.toMemberResponse() = MemberResponse(
    id = UUID.fromString(publicId),
    name = name,
    student = if (hasGrade()) toStudentResponse() else null,
)

fun List<UserInfoMessage>.toMemberResponses() = map { it.toMemberResponse() }

fun OutSleepingDeadlineEntity.toResponse() = DeadlineResponse(
    dayOfWeek = dayOfWeek,
    time = time,
)
