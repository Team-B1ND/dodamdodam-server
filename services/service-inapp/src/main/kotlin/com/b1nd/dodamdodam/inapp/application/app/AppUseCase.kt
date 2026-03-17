package com.b1nd.dodamdodam.inapp.application.app

import com.b1nd.dodamdodam.inapp.application.app.data.response.PageResponse
import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.core.security.passport.holder.PassportHolder
import com.b1nd.dodamdodam.core.security.passport.requireUserId
import com.b1nd.dodamdodam.inapp.application.app.data.request.CreateAppReleaseRequest
import com.b1nd.dodamdodam.inapp.application.app.data.request.CreateAppRequest
import com.b1nd.dodamdodam.inapp.application.app.data.request.CreateAppServerRequest
import com.b1nd.dodamdodam.inapp.application.app.data.request.DenyAppReleaseRequest
import com.b1nd.dodamdodam.inapp.application.app.data.request.DenyAppServerRequest
import com.b1nd.dodamdodam.inapp.application.app.data.request.EditAppRequest
import com.b1nd.dodamdodam.inapp.application.app.data.request.ToggleAppReleaseRequest
import com.b1nd.dodamdodam.inapp.application.app.data.request.ToggleAppServerRequest
import com.b1nd.dodamdodam.inapp.application.app.data.request.UpdateAppReleaseStatusRequest
import com.b1nd.dodamdodam.inapp.application.app.data.request.UpdateAppServerStatusRequest
import com.b1nd.dodamdodam.inapp.application.app.data.response.ActiveAppResponse
import com.b1nd.dodamdodam.inapp.application.app.data.response.AppApiKeyResponse
import com.b1nd.dodamdodam.inapp.application.app.data.response.AppDetailResponse
import com.b1nd.dodamdodam.inapp.application.app.data.response.AppReleaseResponse
import com.b1nd.dodamdodam.inapp.application.app.data.response.AppResponse
import com.b1nd.dodamdodam.inapp.application.app.data.response.AppSummaryResponse
import com.b1nd.dodamdodam.inapp.application.app.data.toActiveAppResponse
import com.b1nd.dodamdodam.inapp.application.app.data.toCommand
import com.b1nd.dodamdodam.inapp.application.app.data.toDetailResponse
import com.b1nd.dodamdodam.inapp.application.app.data.toSummaryResponses
import com.b1nd.dodamdodam.inapp.application.app.data.toResponse
import com.b1nd.dodamdodam.inapp.domain.app.service.AppService
import com.b1nd.dodamdodam.inapp.infrastructure.user.client.UserQueryClient
import kotlinx.coroutines.runBlocking
import org.springframework.data.domain.Pageable
import java.time.LocalDate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Component
@Transactional(rollbackFor = [Exception::class])
class AppUseCase(
    private val appService: AppService,
    private val userQueryClient: UserQueryClient,
) {
    fun createApp(request: CreateAppRequest): Response<AppResponse> {
        val appId = appService.create(currentUserId(), request.toCommand())
        return Response.created("앱이 생성되었어요.", AppResponse(appId))
    }

    fun createServer(request: CreateAppServerRequest): Response<Any> {
        appService.createServer(currentUserId(), request.toCommand())
        return Response.created("앱 서버가 등록되었어요.")
    }

    fun createRelease(request: CreateAppReleaseRequest): Response<Any> {
        val releaseId = appService.createRelease(currentUserId(), request.appId, request.releaseUrl, request.memo)
        return Response.created("릴리즈가 등록되었어요.", mapOf("releaseId" to releaseId))
    }

    fun updateReleaseStatus(request: UpdateAppReleaseStatusRequest): Response<Any> {
        appService.updateReleaseStatus(currentUserId(), request.releaseId, request.status, request.denyResult)
        return Response.ok("릴리즈 상태가 변경되었어요.")
    }

    fun denyRelease(request: DenyAppReleaseRequest): Response<Any> {
        appService.denyRelease(currentUserId(), request.releaseId, request.denyResult)
        return Response.ok("릴리즈가 거절되었어요.")
    }

    fun toggleRelease(request: ToggleAppReleaseRequest): Response<Any> {
        appService.toggleReleaseEnabled(currentUserId(), request.releaseId, request.enabled)
        return Response.ok("릴리즈 활성화 상태가 변경되었어요.")
    }

    fun getReleases(appId: UUID, date: LocalDate?, keyword: String?, pageable: Pageable): Response<PageResponse<AppReleaseResponse>> {
        val releases = appService.getReleases(currentUserId(), appId, date, keyword, pageable)
        val userIds = releases.content.map { it.updatedUser.toString() }.distinct()
        val userMap = runBlocking { userQueryClient.getUsers(userIds) }
            .usersList
            .associateBy { UUID.fromString(it.publicId) }
        return Response.ok("릴리즈 목록을 조회했어요.", PageResponse.of(releases.map { it.toResponse(userMap) }))
    }

    @Transactional(readOnly = true)
    fun getActiveApps(pageable: Pageable): Response<PageResponse<ActiveAppResponse>> {
        val apps = appService.getActiveApps(pageable)
        return Response.ok("서비스 목록을 조회했어요.", PageResponse.of(apps.map { it.toActiveAppResponse() }))
    }

    fun getApp(appId: UUID): Response<AppDetailResponse> {
        val (app, server, releases) = appService.getAppDetail(appId)
        return Response.ok("앱 정보를 조회했어요.", app.toDetailResponse(server, releases))
    }

    fun getAppsByTeam(teamId: UUID): Response<List<AppSummaryResponse>> {
        val apps = appService.getAppsByTeam(currentUserId(), teamId)
        return Response.ok("팀의 앱 목록을 조회했어요.", apps.toSummaryResponses())
    }

    fun getMyApps(): Response<List<AppSummaryResponse>> {
        val apps = appService.getMyApps(currentUserId())
        return Response.ok("내 앱 목록을 조회했어요.", apps.toSummaryResponses())
    }

    fun editApp(request: EditAppRequest): Response<Any> {
        appService.updateApp(currentUserId(), request.toCommand())
        return Response.ok("앱 정보가 수정되었어요.")
    }

    fun updateServerStatus(request: UpdateAppServerStatusRequest): Response<Any> {
        appService.updateServerStatus(request.appId, request.status, request.denyResult)
        return Response.ok("앱 서버 상태가 변경되었어요.")
    }

    fun denyServer(request: DenyAppServerRequest): Response<Any> {
        appService.denyServer(request.appId, request.denyResult)
        return Response.ok("앱 서버가 거절되었어요.")
    }

    fun toggleServer(request: ToggleAppServerRequest): Response<Any> {
        appService.toggleServerEnabled(currentUserId(), request.appId, request.enabled)
        return Response.ok("앱 서버 활성화 상태가 변경되었어요.")
    }

    fun createApiKey(appId: UUID): Response<AppApiKeyResponse> {
        val apiKeyEntity = appService.createApiKey(currentUserId(), appId)
        return Response.created(
            "API Key가 생성되었어요.",
            AppApiKeyResponse(apiKey = apiKeyEntity.rawApiKey!!, expiredAt = apiKeyEntity.expiredAt)
        )
    }

    fun regenerateApiKey(appId: UUID): Response<AppApiKeyResponse> {
        val apiKeyEntity = appService.regenerateApiKey(currentUserId(), appId)
        return Response.ok(
            "API Key가 재발급되었어요.",
            AppApiKeyResponse(apiKey = apiKeyEntity.rawApiKey!!, expiredAt = apiKeyEntity.expiredAt)
        )
    }

    fun deleteApp(appId: UUID): Response<Any> {
        appService.deleteApp(currentUserId(), appId)
        return Response.ok("앱이 삭제되었어요.")
    }

    private fun currentUserId() = PassportHolder.current().requireUserId()
}
