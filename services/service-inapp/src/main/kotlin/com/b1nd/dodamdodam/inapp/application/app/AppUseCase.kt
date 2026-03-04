package com.b1nd.dodamdodam.inapp.application.app

import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.core.security.passport.holder.PassportHolder
import com.b1nd.dodamdodam.core.security.passport.requireUserId
import com.b1nd.dodamdodam.inapp.application.app.data.request.CreateAppReleaseRequest
import com.b1nd.dodamdodam.inapp.application.app.data.request.CreateAppRequest
import com.b1nd.dodamdodam.inapp.application.app.data.request.EditAppRequest
import com.b1nd.dodamdodam.inapp.application.app.data.request.ToggleAppReleaseRequest
import com.b1nd.dodamdodam.inapp.application.app.data.request.UpdateAppReleaseStatusRequest
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
            userId = PassportHolder.current().requireUserId(),
            teamId = request.teamId,
            name = request.name,
            subtitle = request.subtitle,
            description = request.description,
            iconUrl = request.iconUrl,
            darkIconUrl = request.darkIconUrl,
            inquiryMail = request.inquiryMail
        )
        return Response.created("앱이 생성되었어요.", AppResponse(appId))
    }

    fun createRelease(request: CreateAppReleaseRequest): Response<Any> {
        val releaseId = appService.createRelease(
            userId = PassportHolder.current().requireUserId(),
            appId = request.appId,
            releaseUrl = request.releaseUrl,
            memo = request.memo
        )
        return Response.created("릴리즈가 등록되었어요.", mapOf("releaseId" to releaseId))
    }

    fun updateReleaseStatus(request: UpdateAppReleaseStatusRequest): Response<Any> {
        appService.updateReleaseStatus(
            userId = PassportHolder.current().requireUserId(),
            releaseId = request.releaseId,
            status = request.status,
            denyResult = request.denyResult
        )
        return Response.ok("릴리즈 상태가 변경되었어요.")
    }

    fun toggleRelease(request: ToggleAppReleaseRequest): Response<Any> {
        appService.toggleReleaseEnabled(
            userId = PassportHolder.current().requireUserId(),
            releaseId = request.releaseId,
            enabled = request.enabled
        )
        return Response.ok("릴리즈 활성화 상태가 변경되었어요.")
    }

    fun getReleases(appId: UUID): Response<List<AppReleaseResponse>> {
        val releases = appService.getReleases(
            userId = PassportHolder.current().requireUserId(),
            appId = appId
        )
        return Response.ok("릴리즈 목록을 조회했어요.", releases.toResponses())
    }

    fun getApp(appId: UUID): Response<AppDetailResponse> {
        val (app, releases) = appService.getAppDetail(appId)
        return Response.ok("앱 정보를 조회했어요.", app.toDetailResponse(releases))
    }

    fun getAppsByTeam(teamId: UUID): Response<List<AppSummaryResponse>> {
        val apps = appService.getAppsByTeam(
            userId = PassportHolder.current().requireUserId(),
            teamId = teamId
        )
        return Response.ok("팀의 앱 목록을 조회했어요.", apps.toSummaryResponses())
    }

    fun getMyApps(): Response<List<AppSummaryResponse>> {
        val apps = appService.getMyApps(
            userId = PassportHolder.current().requireUserId()
        )
        return Response.ok("내 앱 목록을 조회했어요.", apps.toSummaryResponses())
    }

    fun editApp(request: EditAppRequest): Response<Any> {
        appService.updateApp(
            userId = PassportHolder.current().requireUserId(),
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

    fun deleteApp(appId: UUID): Response<Any> {
        appService.deleteApp(
            userId = PassportHolder.current().requireUserId(),
            appId = appId
        )
        return Response.ok("앱이 삭제되었어요.")
    }

}
