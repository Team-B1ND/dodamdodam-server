package com.b1nd.dodamdodam.nightstudy.application.data

import com.b1nd.dodamdodam.nightstudy.application.data.request.ApplyNightStudyProjectRequest
import com.b1nd.dodamdodam.nightstudy.application.data.request.ApplyNightStudyRequest
import com.b1nd.dodamdodam.nightstudy.application.data.request.CreateNightStudyBanRequest
import com.b1nd.dodamdodam.nightstudy.application.data.response.NightStudyBanResponse
import com.b1nd.dodamdodam.nightstudy.application.data.response.NightStudyProjectResponse
import com.b1nd.dodamdodam.nightstudy.application.data.response.NightStudyResponse
import com.b1nd.dodamdodam.nightstudy.application.data.response.ProjectRoomResponse
import com.b1nd.dodamdodam.nightstudy.domain.ban.entity.NightStudyBanEntity
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.entity.NightStudyEntity
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.entity.NightStudyMemberEntity
import java.util.UUID

fun NightStudyEntity.toResponse(): NightStudyResponse =
    NightStudyResponse(
        id = id!!,
        userId = userId,
        content = content,
        type = type,
        doNeedPhone = doNeedPhone,
        reasonForPhone = reasonForPhone,
        startAt = startAt,
        endAt = endAt,
        status = status,
        rejectReason = rejectReason,
        createdAt = createdAt,
        modifiedAt = modifiedAt
    )

fun ApplyNightStudyRequest.toEntity(userId: UUID): NightStudyEntity =
    NightStudyEntity(
        userId = userId,
        content = content,
        type = type,
        doNeedPhone = doNeedPhone,
        reasonForPhone = reasonForPhone,
        startAt = startAt,
        endAt = endAt
    )

fun NightStudyEntity.toProjectResponse(): NightStudyProjectResponse =
    NightStudyProjectResponse(
        id = id!!,
        userId = userId,
        type = type,
        name = name!!,
        description = content,
        startAt = startAt,
        endAt = endAt,
        status = status,
        room = room,
        rejectReason = rejectReason,
        userIds = members.map { it.userId },
        createdAt = createdAt,
        modifiedAt = modifiedAt
    )

fun ApplyNightStudyProjectRequest.toEntity(userId: UUID): NightStudyEntity {
    val nightStudy = NightStudyEntity(
        userId = userId,
        content = description,
        type = type,
        name = name,
        startAt = startAt,
        endAt = endAt
    )
    val memberEntities = memberUserIds.map { memberId ->
        NightStudyMemberEntity(
            nightStudy = nightStudy,
            userId = memberId
        )
    }
    nightStudy.members.addAll(memberEntities)
    return nightStudy
}

fun NightStudyEntity.toRoomResponse(): ProjectRoomResponse =
    ProjectRoomResponse(
        room = room!!,
        projectId = id!!,
        projectName = name!!
    )

fun NightStudyBanEntity.toResponse(): NightStudyBanResponse =
    NightStudyBanResponse(
        id = id!!,
        userId = userId,
        reason = reason,
        endedAt = endedAt,
        createdAt = createdAt,
        modifiedAt = modifiedAt
    )

fun CreateNightStudyBanRequest.toEntity(): NightStudyBanEntity =
    NightStudyBanEntity(
        userId = userId,
        reason = reason,
        endedAt = endedAt
    )
