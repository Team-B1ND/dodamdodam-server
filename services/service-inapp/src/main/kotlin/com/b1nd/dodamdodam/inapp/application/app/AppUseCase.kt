package com.b1nd.dodamdodam.inapp.application.app

import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.core.security.passport.holder.PassportHolder
import com.b1nd.dodamdodam.core.security.passport.requireUserId
import com.b1nd.dodamdodam.inapp.application.app.data.request.CreateAppReleaseRequest
import com.b1nd.dodamdodam.inapp.application.app.data.request.CreateAppRequest
import com.b1nd.dodamdodam.inapp.application.app.data.request.CreateAppServerRequest
import com.b1nd.dodamdodam.inapp.application.app.data.request.DenyAppReleaseRequest
import com.b1nd.dodamdodam.inapp.application.app.data.request.DenyAppServerRequest
import com.b1nd.dodamdodam.inapp.application.app.data.request.EditAppRequest
import com.b1nd.dodamdodam.inapp.application.app.data.request.EditAppServerRequest
import com.b1nd.dodamdodam.inapp.application.app.data.request.ToggleAppReleaseRequest
import com.b1nd.dodamdodam.inapp.application.app.data.request.ToggleAppServerRequest
import com.b1nd.dodamdodam.inapp.application.app.data.request.UpdateAppReleaseStatusRequest
import com.b1nd.dodamdodam.inapp.application.app.data.request.UpdateAppServerStatusRequest
import com.b1nd.dodamdodam.inapp.application.app.data.response.AppDetailResponse
import com.b1nd.dodamdodam.inapp.application.app.data.response.AppReleaseResponse
import com.b1nd.dodamdodam.inapp.application.app.data.response.AppResponse
import com.b1nd.dodamdodam.inapp.application.app.data.response.AppSummaryResponse
import com.b1nd.dodamdodam.inapp.application.app.data.toDetailResponse
import com.b1nd.dodamdodam.inapp.application.app.data.toResponses
import com.b1nd.dodamdodam.inapp.application.app.data.toSummaryResponses
import com.b1nd.dodamdodam.inapp.domain.app.service.AppService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Component
@Transactional(rollbackFor = [Exception::class])
class AppUseCase(
    private val appService: AppService
) {
    fun createApp(request: CreateAppRequest): Response<AppResponse> {
        val appId = appService.create(
            userId = currentUserId(),
            teamId = request.teamId,
            name = request.name,
            subtitle = request.subtitle,
            description = request.description,
            iconUrl = request.iconUrl,
            darkIconUrl = request.darkIconUrl,
            inquiryMail = request.inquiryMail,
            serverName = request.server?.name,
            serverAddress = request.server?.serverAddress,
            redirectPath = request.server?.redirectPath,
            prefixLevel = request.server?.omitApiPrefix?.toPrefixLevel()
        )
        return Response.created("앱이 생성되었어요.", AppResponse(appId))
    }

    fun createServer(request: CreateAppServerRequest): Response<Any> {
        appService.createServer(
            userId = currentUserId(),
            appId = request.appId,
            name = request.name,
            serverAddress = request.serverAddress,
            redirectPath = request.redirectPath,
            prefixLevel = request.omitApiPrefix.toPrefixLevel()
        )
        return Response.created("앱 서버가 등록되었어요.")
    }

    fun createRelease(request: CreateAppReleaseRequest): Response<Any> {
        val releaseId = appService.createRelease(
            userId = currentUserId(),
            appId = request.appId,
            releaseUrl = request.releaseUrl,
            memo = request.memo
        )
        return Response.created("릴리즈가 등록되었어요.", mapOf("releaseId" to releaseId))
    }

    fun updateReleaseStatus(request: UpdateAppReleaseStatusRequest): Response<Any> {
        appService.updateReleaseStatus(
            userId = currentUserId(),
            releaseId = request.releaseId,
            status = request.status,
            denyResult = request.denyResult
        )
        return Response.ok("릴리즈 상태가 변경되었어요.")
    }

    fun denyRelease(request: DenyAppReleaseRequest): Response<Any> {
        appService.denyRelease(
            userId = currentUserId(),
            releaseId = request.releaseId,
            denyResult = request.denyResult
        )
        return Response.ok("릴리즈가 거절되었어요.")
    }

    fun toggleRelease(request: ToggleAppReleaseRequest): Response<Any> {
        appService.toggleReleaseEnabled(
            userId = currentUserId(),
            releaseId = request.releaseId,
            enabled = request.enabled
        )
        return Response.ok("릴리즈 활성화 상태가 변경되었어요.")
    }

    fun getReleases(appId: UUID): Response<List<AppReleaseResponse>> {
        val releases = appService.getReleases(
            userId = currentUserId(),
            appId = appId
        )
        return Response.ok("릴리즈 목록을 조회했어요.", releases.toResponses())
    }

    fun getApp(appId: UUID): Response<AppDetailResponse> {
        val (app, server, releases) = appService.getAppDetail(appId)
        return Response.ok("앱 정보를 조회했어요.", app.toDetailResponse(server, releases))
    }

    fun getAppsByTeam(teamId: UUID): Response<List<AppSummaryResponse>> {
        val apps = appService.getAppsByTeam(
            userId = currentUserId(),
            teamId = teamId
        )
        return Response.ok("팀의 앱 목록을 조회했어요.", apps.toSummaryResponses())
    }

    fun getMyApps(): Response<List<AppSummaryResponse>> {
        val apps = appService.getMyApps(
            userId = currentUserId()
        )
        return Response.ok("내 앱 목록을 조회했어요.", apps.toSummaryResponses())
    }

    fun editApp(request: EditAppRequest): Response<Any> {
        appService.updateApp(
            userId = currentUserId(),
            appId = request.appId,
            name = request.name,
            subtitle = request.subtitle,
            description = request.description,
            iconUrl = request.iconUrl,
            darkIconUrl = request.darkIconUrl,
            inquiryMail = request.inquiryMail
        )
        return Response.ok("앱 정보가 수정되었어요.")
    }

    fun editServer(request: EditAppServerRequest): Response<Any> {
        appService.updateServer(
            userId = currentUserId(),
            appId = request.appId,
            name = request.name,
            serverAddress = request.serverAddress,
            redirectPath = request.redirectPath,
            prefixLevel = request.omitApiPrefix?.toPrefixLevel()
        )
        return Response.ok("앱 서버 정보가 수정되었어요.")
    }

    fun updateServerStatus(request: UpdateAppServerStatusRequest): Response<Any> {
        appService.updateServerStatus(
            appId = request.appId,
            status = request.status,
            denyResult = request.denyResult
        )
        return Response.ok("앱 서버 상태가 변경되었어요.")
    }

    fun denyServer(request: DenyAppServerRequest): Response<Any> {
        appService.denyServer(
            appId = request.appId,
            denyResult = request.denyResult
        )
        return Response.ok("앱 서버가 거절되었어요.")
    }

    fun toggleServer(request: ToggleAppServerRequest): Response<Any> {
        appService.toggleServerEnabled(
            userId = currentUserId(),
            appId = request.appId,
            enabled = request.enabled
        )
        return Response.ok("앱 서버 활성화 상태가 변경되었어요.")
    }

    fun deleteApp(appId: UUID): Response<Any> {
        appService.deleteApp(
            userId = currentUserId(),
            appId = appId
        )
        return Response.ok("앱이 삭제되었어요.")
    }

    private fun currentUserId() = PassportHolder.current().requireUserId()

    private fun Boolean.toPrefixLevel() = if (this) 1 else 0
}
