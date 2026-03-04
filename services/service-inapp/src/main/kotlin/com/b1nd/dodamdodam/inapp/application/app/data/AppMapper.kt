package com.b1nd.dodamdodam.inapp.application.app.data

import com.b1nd.dodamdodam.inapp.application.app.data.response.AppDetailResponse
import com.b1nd.dodamdodam.inapp.application.app.data.response.AppReleaseResponse
import com.b1nd.dodamdodam.inapp.application.app.data.response.AppServerResponse
import com.b1nd.dodamdodam.inapp.application.app.data.response.AppSummaryResponse
import com.b1nd.dodamdodam.inapp.domain.app.entity.AppEntity
import com.b1nd.dodamdodam.inapp.domain.app.entity.AppReleaseEntity
import com.b1nd.dodamdodam.inapp.domain.app.entity.AppServerEntity

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

fun AppServerEntity.toResponse() = AppServerResponse(
    name = name,
    serverAddress = serverAddress,
    redirectPath = redirectPath,
    prefixLevel = prefixLevel,
    omitApiPrefix = prefixLevel == 1,
    enabled = enabled,
    status = status,
    denyResult = denyResult,
)

fun AppEntity.toDetailResponse(
    server: AppServerEntity?,
    releases: List<AppReleaseEntity>
) = AppDetailResponse(
    appId = publicId!!,
    teamId = team.publicId!!,
    name = name,
    subtitle = subtitle,
    description = description,
    iconUrl = iconUrl,
    darkIconUrl = darkIconUrl,
    inquiryMail = inquiryMail,
    server = server?.toResponse(),
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
