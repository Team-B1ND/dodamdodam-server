package com.b1nd.dodamdodam.nightstudy.application.nightstudy.data

import com.b1nd.dodamdodam.nightstudy.application.nightstudy.data.request.ApplyPersonalNightStudyRequest
import com.b1nd.dodamdodam.nightstudy.application.nightstudy.data.request.ApplyProjectNightStudyRequest
import com.b1nd.dodamdodam.nightstudy.application.nightstudy.data.response.PersonalNightStudyResponse
import com.b1nd.dodamdodam.nightstudy.application.nightstudy.data.response.ProjectNightStudyResponse
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.entity.NightStudyEntity
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.enumeration.NightStudyType
import java.util.UUID

fun ApplyPersonalNightStudyRequest.toEntity(userId: UUID) = NightStudyEntity(
    leaderId = userId,
    type = NightStudyType.PERSONAL,
    description = description,
    period = period,
    startAt = startAt,
    endAt = endAt,
    needPhone = needPhone,
    needPhoneReason = needPhoneReason,
)

fun ApplyProjectNightStudyRequest.toEntity(userId: UUID) = NightStudyEntity(
    name = name,
    description = description,
    period = period,
    startAt = startAt,
    endAt = endAt,
    leaderId = userId,
    type = NightStudyType.PROJECT,
    needPhone = false
)

fun NightStudyEntity.toPersonalNightStudyResponse() = PersonalNightStudyResponse(
    id = id!!,
    description = description,
    period = period,
    startAt = startAt,
    endAt = endAt,
    needPhone = needPhone,
    needPhoneReason = needPhoneReason,
    rejectionReason = rejectionReason,
)

fun List<NightStudyEntity>.toPersonalNightStudyListResponse() = map { it.toPersonalNightStudyResponse() }

fun NightStudyEntity.toProjectNightStudyResponse() = ProjectNightStudyResponse(
    id = id!!,
    description = description,
    period = period,
    startAt = startAt,
    endAt = endAt,
    name = name!!,
    rejectionReason = rejectionReason,
)

fun List<NightStudyEntity>.toProjectNightStudyListResponse() = map { it.toProjectNightStudyResponse() }