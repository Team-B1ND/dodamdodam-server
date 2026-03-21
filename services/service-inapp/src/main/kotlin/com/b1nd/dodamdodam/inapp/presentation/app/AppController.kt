package com.b1nd.dodamdodam.inapp.presentation.app

import com.b1nd.dodamdodam.core.security.annotation.authentication.UserAccess
import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
import com.b1nd.dodamdodam.inapp.application.app.AppUseCase
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
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.util.UUID

@RestController
@RequestMapping("/app")
class AppController(
    private val appUseCase: AppUseCase
) {
    @UserAccess
    @PostMapping
    fun createApp(@RequestBody request: CreateAppRequest) =
        appUseCase.createApp(request)

    @UserAccess
    @PatchMapping
    fun editApp(@RequestBody request: EditAppRequest) =
        appUseCase.editApp(request)

    @UserAccess
    @DeleteMapping("/{appId}")
    fun deleteApp(@PathVariable appId: UUID) =
        appUseCase.deleteApp(appId)

    @UserAccess
    @PostMapping("/release")
    fun createRelease(@RequestBody request: CreateAppReleaseRequest) =
        appUseCase.createRelease(request)

    @UserAccess
    @PostMapping("/server")
    fun createServer(@RequestBody request: CreateAppServerRequest) =
        appUseCase.createServer(request)

    @UserAccess(roles = [RoleType.ADMIN])
    @PatchMapping("/release/status")
    fun updateReleaseStatus(@RequestBody request: UpdateAppReleaseStatusRequest) =
        appUseCase.updateReleaseStatus(request)

    @UserAccess(roles = [RoleType.ADMIN])
    @PatchMapping("/release/deny")
    fun denyRelease(@RequestBody request: DenyAppReleaseRequest) =
        appUseCase.denyRelease(request)

    @UserAccess
    @PatchMapping("/release/toggle")
    fun toggleRelease(@RequestBody request: ToggleAppReleaseRequest) =
        appUseCase.toggleRelease(request)

    @UserAccess(roles = [RoleType.ADMIN])
    @PatchMapping("/server/status")
    fun updateServerStatus(@RequestBody request: UpdateAppServerStatusRequest) =
        appUseCase.updateServerStatus(request)

    @UserAccess(roles = [RoleType.ADMIN])
    @PatchMapping("/server/deny")
    fun denyServer(@RequestBody request: DenyAppServerRequest) =
        appUseCase.denyServer(request)

    @UserAccess
    @PatchMapping("/server/toggle")
    fun toggleServer(@RequestBody request: ToggleAppServerRequest) =
        appUseCase.toggleServer(request)

    @UserAccess
    @PostMapping("/{appId}/api-key")
    fun issueApiKey(@PathVariable appId: UUID) =
        appUseCase.issueApiKey(appId)

    @UserAccess
    @GetMapping("/{appId}/api-key")
    fun getApiKeys(@PathVariable appId: UUID) =
        appUseCase.getApiKeys(appId)

    @UserAccess
    @GetMapping("/active")
    fun getActiveApps(pageable: Pageable) =
        appUseCase.getActiveApps(pageable)

    @UserAccess
    @GetMapping("/{appId}")
    fun getApp(@PathVariable appId: UUID) =
        appUseCase.getApp(appId)

    @UserAccess
    @GetMapping("/me")
    fun getMyApps() =
        appUseCase.getMyApps()

    @UserAccess
    @GetMapping("/team/{teamId}")
    fun getAppsByTeam(@PathVariable teamId: UUID) =
        appUseCase.getAppsByTeam(teamId)

    @UserAccess
    @GetMapping("/release/{releaseId}")
    fun getReleaseDetail(@PathVariable releaseId: UUID) =
        appUseCase.getReleaseDetail(releaseId)

    @UserAccess
    @GetMapping("/{appId}/release")
    fun getReleases(
        @PathVariable appId: UUID,
        @RequestParam(required = false) date: LocalDate?,
        @RequestParam(required = false) keyword: String?,
        pageable: Pageable,
    ) = appUseCase.getReleases(appId, date, keyword, pageable)
}
