package com.b1nd.dodamdodam.inapp.domain.app.service

import com.b1nd.dodamdodam.inapp.domain.app.entity.AppEntity
import com.b1nd.dodamdodam.inapp.domain.app.entity.AppReleaseEntity
import com.b1nd.dodamdodam.inapp.domain.app.entity.AppServerEntity
import com.b1nd.dodamdodam.inapp.domain.app.enumeration.AppReleaseStatusType
import com.b1nd.dodamdodam.inapp.domain.app.enumeration.AppServerStatusType
import com.b1nd.dodamdodam.inapp.domain.app.exception.AppAlreadyExistException
import com.b1nd.dodamdodam.inapp.domain.app.exception.AppDenyReasonRequiredException
import com.b1nd.dodamdodam.inapp.domain.app.exception.AppNotFoundException
import com.b1nd.dodamdodam.inapp.domain.app.exception.AppReleaseEnableNotAllowedException
import com.b1nd.dodamdodam.inapp.domain.app.exception.AppReleaseNotFoundException
import com.b1nd.dodamdodam.inapp.domain.app.exception.AppServerAlreadyExistException
import com.b1nd.dodamdodam.inapp.domain.app.exception.AppServerEnableNotAllowedException
import com.b1nd.dodamdodam.inapp.domain.app.exception.AppServerNotFoundException
import com.b1nd.dodamdodam.inapp.domain.app.exception.AppServerPrefixLevelInvalidException
import com.b1nd.dodamdodam.inapp.domain.app.exception.AppServerRedirectPathInvalidException
import com.b1nd.dodamdodam.inapp.domain.app.exception.AppTeamMemberPermissionRequiredException
import com.b1nd.dodamdodam.inapp.domain.app.exception.AppTeamOwnerPermissionRequiredException
import com.b1nd.dodamdodam.inapp.domain.app.repository.AppReleaseRepository
import com.b1nd.dodamdodam.inapp.domain.app.repository.AppRepository
import com.b1nd.dodamdodam.inapp.domain.app.repository.AppServerRepository
import com.b1nd.dodamdodam.inapp.domain.team.entity.TeamEntity
import com.b1nd.dodamdodam.inapp.domain.team.repository.TeamMemberRepository
import com.b1nd.dodamdodam.inapp.domain.team.repository.TeamRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AppService(
    private val appRepository: AppRepository,
    private val appReleaseRepository: AppReleaseRepository,
    private val appServerRepository: AppServerRepository,
    private val teamRepository: TeamRepository,
    private val teamMemberRepository: TeamMemberRepository
) {
    companion object {
        private val REDIRECT_PATH_REGEX = Regex("^/([A-Za-z0-9_-]+)(/[A-Za-z0-9_-]+)*$")
    }

    fun create(
        userId: UUID,
        teamId: UUID,
        name: String,
        subtitle: String,
        description: String?,
        iconUrl: String,
        darkIconUrl: String?,
        inquiryMail: String,
        serverName: String?,
        serverAddress: String?,
        redirectPath: String?,
        prefixLevel: Int?
    ): UUID {
        if (existByName(name)) throw AppAlreadyExistException()
        val team = getTeamWithMemberPermission(userId, teamId)
        val app = appRepository.save(AppEntity(name, description, subtitle, iconUrl, darkIconUrl, inquiryMail, team))
        if (serverName != null && serverAddress != null && redirectPath != null && prefixLevel != null) {
            createServerInternal(app, serverName, serverAddress, redirectPath, prefixLevel)
        }
        return app.publicId!!
    }

    fun createServer(
        userId: UUID,
        appId: UUID,
        name: String,
        serverAddress: String,
        redirectPath: String,
        prefixLevel: Int
    ) {
        val app = getAppWithMemberPermission(userId, appId)
        createServerInternal(app, name, serverAddress, redirectPath, prefixLevel)
    }

    fun createRelease(userId: UUID, appId: UUID, releaseUrl: String, memo: String?): UUID {
        val app = getAppWithMemberPermission(userId, appId)
        val release = appReleaseRepository.save(
            AppReleaseEntity(
                app = app,
                enabled = false,
                releaseUrl = releaseUrl,
                updatedUser = userId,
                memo = memo,
                status = AppReleaseStatusType.PENDING
            )
        )
        return release.publicId!!
    }

    fun updateReleaseStatus(userId: UUID, releaseId: UUID, status: AppReleaseStatusType, denyResult: String?) {
        if (status == AppReleaseStatusType.DENIED) {
            requireDenyReason(denyResult)
        }
        val release = getRelease(releaseId)
        release.updateStatus(status, denyResult, userId)
    }

    fun denyRelease(userId: UUID, releaseId: UUID, denyResult: String?) {
        updateReleaseStatus(userId, releaseId, AppReleaseStatusType.DENIED, denyResult)
    }

    fun toggleReleaseEnabled(userId: UUID, releaseId: UUID, enabled: Boolean) {
        val release = getReleaseWithOwnerPermission(userId, releaseId)
        if (enabled && release.status != AppReleaseStatusType.ALLOWED) {
            throw AppReleaseEnableNotAllowedException()
        }
        if (enabled) {
            appReleaseRepository.findAllByAppAndEnabledIsTrue(release.app)
                .filter { it.id != release.id }
                .forEach { it.updateEnabled(false, userId) }
        }
        release.updateEnabled(enabled, userId)
    }

    fun getReleases(userId: UUID, appId: UUID): List<AppReleaseEntity> {
        val app = getAppWithOwnerPermission(userId, appId)
        return appReleaseRepository.findAllByAppOrderByCreatedAtDesc(app)
    }

    fun getAppDetail(appId: UUID): Triple<AppEntity, AppServerEntity?, List<AppReleaseEntity>> {
        val app = getApp(appId)
        val server = appServerRepository.findByApp(app)
        val releases = appReleaseRepository.findAllByAppOrderByCreatedAtDesc(app)
        return Triple(app, server, releases)
    }

    fun getAppsByTeam(userId: UUID, teamId: UUID): List<AppEntity> {
        val team = getTeamWithMemberPermission(userId, teamId)
        return appRepository.findAllByTeamOrderByIdDesc(team)
    }

    fun getMyApps(userId: UUID): List<AppEntity> {
        val teams = teamMemberRepository.findAllByUser(userId)
            .map { it.team }
            .distinctBy { it.id }
        if (teams.isEmpty()) return emptyList()
        return appRepository.findAllByTeamInOrderByIdDesc(teams)
    }

    fun updateApp(
        userId: UUID,
        appId: UUID,
        name: String?,
        subtitle: String?,
        description: String?,
        iconUrl: String?,
        darkIconUrl: String?,
        inquiryMail: String?
    ) {
        val app = getAppWithMemberPermission(userId, appId)
        name?.let {
            if (it != app.name && existByName(it)) throw AppAlreadyExistException()
        }
        app.update(name, subtitle, description, iconUrl, darkIconUrl, inquiryMail)
    }

    fun updateServer(
        userId: UUID,
        appId: UUID,
        name: String?,
        serverAddress: String?,
        redirectPath: String?,
        prefixLevel: Int?
    ) {
        val app = getAppWithMemberPermission(userId, appId)
        val server = getAppServer(app)
        server.updateServerInfo(
            name = name,
            serverAddress = serverAddress,
            redirectPath = redirectPath?.let { normalizeAndValidatePath(it) },
            prefixLevel = prefixLevel?.let { normalizePrefixLevel(it) }
        )
    }

    fun updateServerStatus(appId: UUID, status: AppServerStatusType, denyResult: String?) {
        if (status == AppServerStatusType.DENIED) {
            requireDenyReason(denyResult)
        }
        val app = getApp(appId)
        val server = getAppServer(app)
        server.updateStatus(status, denyResult)
    }

    fun denyServer(appId: UUID, denyResult: String?) {
        updateServerStatus(appId, AppServerStatusType.DENIED, denyResult)
    }

    fun toggleServerEnabled(userId: UUID, appId: UUID, enabled: Boolean) {
        val app = getAppWithMemberPermission(userId, appId)
        val server = getAppServer(app)
        if (enabled && server.status != AppServerStatusType.ALLOWED) {
            throw AppServerEnableNotAllowedException()
        }
        server.updateEnabled(enabled)
    }

    fun deleteApp(userId: UUID, appId: UUID) {
        val app = getAppWithOwnerPermission(userId, appId)
        appRepository.delete(app)
    }

    fun existByName(name: String) =
        appRepository.existsByName(name)

    fun getApp(appId: UUID): AppEntity =
        appRepository.findByPublicId(appId)
            ?: throw AppNotFoundException()

    private fun getRelease(releaseId: UUID): AppReleaseEntity =
        appReleaseRepository.findByPublicId(releaseId)
            ?: throw AppReleaseNotFoundException()

    private fun validateAppOwner(userId: UUID, app: AppEntity) {
        if (!teamMemberRepository.existsByUserAndTeamAndIsOwnerIsTrue(userId, app.team)) {
            throw AppTeamOwnerPermissionRequiredException()
        }
    }

    private fun getTeamWithMemberPermission(userId: UUID, teamId: UUID): TeamEntity {
        val team = teamRepository.findByPublicId(teamId)
            ?: throw AppTeamMemberPermissionRequiredException()
        if (!teamMemberRepository.existsByUserAndTeam(userId, team)) {
            throw AppTeamMemberPermissionRequiredException()
        }
        return team
    }

    private fun validateAppMember(userId: UUID, app: AppEntity) {
        if (!teamMemberRepository.existsByUserAndTeam(userId, app.team)) {
            throw AppTeamMemberPermissionRequiredException()
        }
    }

    private fun getAppWithMemberPermission(userId: UUID, appId: UUID): AppEntity =
        getApp(appId).also { validateAppMember(userId, it) }

    private fun getAppWithOwnerPermission(userId: UUID, appId: UUID): AppEntity =
        getApp(appId).also { validateAppOwner(userId, it) }

    private fun getReleaseWithOwnerPermission(userId: UUID, releaseId: UUID): AppReleaseEntity =
        getRelease(releaseId).also { validateAppOwner(userId, it.app) }

    private fun getAppServer(app: AppEntity): AppServerEntity =
        appServerRepository.findByApp(app)
            ?: throw AppServerNotFoundException()

    private fun createServerInternal(
        app: AppEntity,
        name: String,
        serverAddress: String,
        redirectPath: String,
        prefixLevel: Int
    ) {
        if (appServerRepository.existsByApp(app)) throw AppServerAlreadyExistException()
        appServerRepository.save(
            AppServerEntity(
                app = app,
                name = name,
                serverAddress = serverAddress,
                redirectPath = normalizeAndValidatePath(redirectPath),
                prefixLevel = normalizePrefixLevel(prefixLevel),
                enabled = false,
                status = AppServerStatusType.PENDING
            )
        )
    }

    private fun normalizePrefixLevel(prefixLevel: Int): Int {
        if (prefixLevel == 0 || prefixLevel == 1) return prefixLevel
        throw AppServerPrefixLevelInvalidException()
    }

    private fun normalizeAndValidatePath(path: String): String {
        val normalized = if (path.startsWith("/")) path else "/$path"
        if (!REDIRECT_PATH_REGEX.matches(normalized)) {
            throw AppServerRedirectPathInvalidException()
        }
        return normalized
    }

    private fun requireDenyReason(denyResult: String?) {
        if (denyResult.isNullOrBlank()) {
            throw AppDenyReasonRequiredException()
        }
    }
}
