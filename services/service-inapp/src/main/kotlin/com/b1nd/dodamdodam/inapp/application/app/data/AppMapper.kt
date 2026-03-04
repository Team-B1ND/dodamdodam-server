package com.b1nd.dodamdodam.inapp.application.app.data

import com.b1nd.dodamdodam.inapp.application.app.data.response.AppDetailResponse
import com.b1nd.dodamdodam.inapp.application.app.data.response.AppReleaseResponse
import com.b1nd.dodamdodam.inapp.application.app.data.response.AppSummaryResponse
import com.b1nd.dodamdodam.inapp.domain.app.entity.AppEntity
import com.b1nd.dodamdodam.inapp.domain.app.entity.AppReleaseEntity

fun AppReleaseEntity.toResponse() = AppReleaseResponse(
    releaseId = publicId!!,
    releaseUrl = releaseUrl,
    memo = memo,
    denyResult = denyResult,
    status = status,
    enabled = enabled,
    updatedUser = updatedUser,
    createdAt = createdAt,
    modifiedAt = modifiedAt,
)

fun List<AppReleaseEntity>.toResponses() = map { it.toResponse() }

fun AppEntity.toDetailResponse(releases: List<AppReleaseEntity>) = AppDetailResponse(
    appId = publicId!!,
    teamId = team.publicId!!,
    name = name,
    subtitle = subtitle,
    description = description,
    iconUrl = iconUrl,
    darkIconUrl = darkIconUrl,
    inquiryMail = inquiryMail,
    active = releases.any { it.enabled },
    releases = releases.toResponses(),
)

fun AppEntity.toSummaryResponse() = AppSummaryResponse(
    appId = publicId!!,
    teamId = team.publicId!!,
    name = name,
    subtitle = subtitle,
    description = description,
    iconUrl = iconUrl,
    darkIconUrl = darkIconUrl,
    inquiryMail = inquiryMail,
)

fun List<AppEntity>.toSummaryResponses() = map { it.toSummaryResponse() }
