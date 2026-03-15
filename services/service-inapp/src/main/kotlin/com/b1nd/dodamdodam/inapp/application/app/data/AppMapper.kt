package com.b1nd.dodamdodam.inapp.application.app.data

import com.b1nd.dodamdodam.inapp.application.app.data.request.AppServerInfoRequest
import com.b1nd.dodamdodam.inapp.application.app.data.request.CreateAppRequest
import com.b1nd.dodamdodam.inapp.application.app.data.request.CreateAppServerRequest
import com.b1nd.dodamdodam.inapp.application.app.data.request.EditAppRequest
import com.b1nd.dodamdodam.inapp.application.app.data.request.EditAppServerRequest
import com.b1nd.dodamdodam.inapp.application.app.data.response.AppDetailResponse
import com.b1nd.dodamdodam.inapp.application.app.data.response.AppReleaseResponse
import com.b1nd.dodamdodam.inapp.application.app.data.response.AppServerResponse
import com.b1nd.dodamdodam.inapp.application.app.data.response.AppSummaryResponse
import com.b1nd.dodamdodam.inapp.domain.app.command.CreateAppCommand
import com.b1nd.dodamdodam.inapp.domain.app.command.CreateServerCommand
import com.b1nd.dodamdodam.inapp.domain.app.command.EditAppCommand
import com.b1nd.dodamdodam.inapp.domain.app.command.EditServerCommand
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
    usePushNotification = usePushNotification,
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
    releaseEnabled = releaseEnabled,
    releaseStatus = releaseStatus,
)

fun List<AppEntity>.toSummaryResponses() = map { it.toSummaryResponse() }

private fun Boolean.toPrefixLevel() = if (this) 1 else 0

fun AppServerInfoRequest.toCreateServerCommand() = CreateServerCommand(
    appId = null,
    name = name,
    serverAddress = serverAddress,
    redirectPath = redirectPath,
    prefixLevel = omitApiPrefix.toPrefixLevel(),
    usePushNotification = usePushNotification,
)

fun CreateAppRequest.toCommand() = CreateAppCommand(
    teamId = teamId,
    name = name,
    subtitle = subtitle,
    description = description,
    iconUrl = iconUrl,
    darkIconUrl = darkIconUrl,
    inquiryMail = inquiryMail,
    githubReleaseUrl = githubReleaseUrl,
    server = server?.toCreateServerCommand(),
)

fun CreateAppServerRequest.toCommand() = CreateServerCommand(
    appId = appId,
    name = name,
    serverAddress = serverAddress,
    redirectPath = redirectPath,
    prefixLevel = omitApiPrefix.toPrefixLevel(),
    usePushNotification = usePushNotification,
)

fun EditAppRequest.toCommand() = EditAppCommand(
    appId = appId,
    name = name,
    subtitle = subtitle,
    description = description,
    iconUrl = iconUrl,
    darkIconUrl = darkIconUrl,
    inquiryMail = inquiryMail,
)

fun EditAppServerRequest.toCommand() = EditServerCommand(
    appId = appId,
    name = name,
    serverAddress = serverAddress,
    redirectPath = redirectPath,
    prefixLevel = omitApiPrefix?.toPrefixLevel(),
    usePushNotification = usePushNotification,
)
