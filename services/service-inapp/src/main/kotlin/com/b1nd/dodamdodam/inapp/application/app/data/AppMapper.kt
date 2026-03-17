package com.b1nd.dodamdodam.inapp.application.app.data

import com.b1nd.dodamdodam.inapp.application.app.data.request.AppServerInfoRequest
import com.b1nd.dodamdodam.inapp.application.app.data.request.CreateAppRequest
import com.b1nd.dodamdodam.inapp.application.app.data.request.CreateAppServerRequest
import com.b1nd.dodamdodam.inapp.application.app.data.request.EditAppRequest
import com.b1nd.dodamdodam.inapp.application.app.data.request.EditAppServerInfoRequest
import com.b1nd.dodamdodam.inapp.application.app.data.request.EditAppServerRequest
import com.b1nd.dodamdodam.grpc.user.UserResponse
import com.b1nd.dodamdodam.inapp.application.app.data.response.ActiveAppResponse
import com.b1nd.dodamdodam.inapp.application.app.data.response.AppDetailResponse
import com.b1nd.dodamdodam.inapp.application.app.data.response.AppReleaseResponse
import com.b1nd.dodamdodam.inapp.application.app.data.response.AppServerResponse
import com.b1nd.dodamdodam.inapp.application.app.data.response.AppSummaryResponse
import com.b1nd.dodamdodam.inapp.application.app.data.response.ReleaseUserResponse
import com.b1nd.dodamdodam.inapp.domain.app.command.CreateAppCommand
import com.b1nd.dodamdodam.inapp.domain.app.command.CreateServerCommand
import com.b1nd.dodamdodam.inapp.domain.app.command.EditAppCommand
import com.b1nd.dodamdodam.inapp.domain.app.command.EditServerCommand
import com.b1nd.dodamdodam.inapp.domain.app.entity.AppEntity
import com.b1nd.dodamdodam.inapp.domain.app.entity.AppReleaseEntity
import com.b1nd.dodamdodam.inapp.domain.app.entity.AppServerEntity
import java.util.UUID

fun AppReleaseEntity.toResponse(userMap: Map<UUID, UserResponse>) = AppReleaseResponse(
    releaseId = publicId!!,
    releaseUrl = releaseUrl,
    memo = memo,
    denyResult = denyResult,
    status = status,
    enabled = enabled,
    updatedUser = userMap[updatedUser]?.let {
        ReleaseUserResponse(
            userId = updatedUser,
            name = it.name,
            profileImage = if (it.hasProfileImage()) it.profileImage else null,
        )
    },
    createdAt = createdAt,
    modifiedAt = modifiedAt,
)

fun List<AppReleaseEntity>.toResponses(userMap: Map<UUID, UserResponse>) = map { it.toResponse(userMap) }

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
    releases: List<AppReleaseEntity>,
    userMap: Map<UUID, UserResponse> = emptyMap(),
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
    releases = releases.toResponses(userMap),
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

fun AppEntity.toActiveAppResponse() = ActiveAppResponse(
    appId = publicId!!,
    name = name,
    subtitle = subtitle,
    description = description,
    iconUrl = iconUrl,
    darkIconUrl = darkIconUrl,
)

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
    server = server?.toEditServerCommand(),
)

fun EditAppServerInfoRequest.toEditServerCommand() = EditServerCommand(
    appId = null,
    name = name,
    serverAddress = serverAddress,
    redirectPath = redirectPath,
    prefixLevel = omitApiPrefix?.toPrefixLevel(),
    usePushNotification = usePushNotification,
)

fun EditAppServerRequest.toCommand() = EditServerCommand(
    appId = appId,
    name = name,
    serverAddress = serverAddress,
    redirectPath = redirectPath,
    prefixLevel = omitApiPrefix?.toPrefixLevel(),
    usePushNotification = usePushNotification,
)
