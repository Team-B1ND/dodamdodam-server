package com.b1nd.dodamdodam.inapp.domain.app.service

import com.b1nd.dodamdodam.inapp.domain.app.command.CreateAppCommand
import com.b1nd.dodamdodam.inapp.domain.app.command.CreateServerCommand
import com.b1nd.dodamdodam.inapp.domain.app.command.EditAppCommand
import com.b1nd.dodamdodam.inapp.domain.app.command.EditServerCommand
import com.b1nd.dodamdodam.inapp.domain.app.entity.AppApiKeyEntity
import com.b1nd.dodamdodam.inapp.domain.app.entity.AppEntity
import com.b1nd.dodamdodam.inapp.domain.app.entity.AppReleaseEntity
import com.b1nd.dodamdodam.inapp.domain.app.entity.AppServerEntity
import com.b1nd.dodamdodam.inapp.domain.app.enumeration.AppStatusType
import com.b1nd.dodamdodam.inapp.domain.app.exception.AppAlreadyExistException
import com.b1nd.dodamdodam.inapp.domain.app.exception.AppApiKeyAlreadyExistException
import com.b1nd.dodamdodam.inapp.domain.app.exception.AppApiKeyNotFoundException
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
import com.b1nd.dodamdodam.inapp.domain.app.repository.AppApiKeyRepository
import com.b1nd.dodamdodam.inapp.domain.app.repository.AppReleaseRepository
import com.b1nd.dodamdodam.inapp.domain.app.repository.AppRepository
import com.b1nd.dodamdodam.inapp.domain.app.repository.AppServerRepository
import com.b1nd.dodamdodam.inapp.infrastructure.kafka.producer.AppApiKeyEventProducer
import com.b1nd.dodamdodam.inapp.infrastructure.kafka.producer.AppServerRouteEventProducer
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import com.b1nd.dodamdodam.inapp.domain.team.entity.TeamEntity
import com.b1nd.dodamdodam.inapp.domain.team.repository.TeamMemberRepository
import com.b1nd.dodamdodam.inapp.domain.team.repository.TeamRepository
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.time.LocalDateTime
import java.util.UUID

@Service
class AppService(
    private val appRepository: AppRepository,
    private val appReleaseRepository: AppReleaseRepository,
    private val appServerRepository: AppServerRepository,
    private val appApiKeyRepository: AppApiKeyRepository,
    private val appServerRouteEventProducer: AppServerRouteEventProducer,
    private val appApiKeyEventProducer: AppApiKeyEventProducer,
    private val teamRepository: TeamRepository,
    private val teamMemberRepository: TeamMemberRepository,
    private val passwordEncoder: BCryptPasswordEncoder
) {
    companion object {
        private val REDIRECT_PATH_REGEX = Regex("^/([A-Za-z0-9_-]+)(/[A-Za-z0-9_-]+)*$")
        private const val API_KEY_EXPIRE_DAYS = 90L
    }

    fun create(userId: UUID, command: CreateAppCommand): UUID {
        if (existByName(command.name)) throw AppAlreadyExistException()
        val team = getTeamWithMemberPermission(userId, command.teamId)
        val app = appRepository.save(command.toEntity(team))
        appReleaseRepository.save(
            AppReleaseEntity(
                app = app,
                enabled = false,
                releaseUrl = command.githubReleaseUrl,
                updatedUser = userId,
                status = AppStatusType.PENDING
            )
        )
        app.updateReleaseInfo(enabled = false, status = AppStatusType.PENDING)
        command.server?.let { saveServer(app, it) }
        return app.publicId!!
    }

    fun createServer(userId: UUID, command: CreateServerCommand) {
        val app = getAppWithMemberPermission(userId, command.appId!!)
        if (appServerRepository.existsByApp(app)) throw AppServerAlreadyExistException()
        saveServer(app, command)
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
                status = AppStatusType.PENDING
            )
        )
        return release.publicId!!
    }

    fun updateReleaseStatus(userId: UUID, releaseId: UUID, status: AppStatusType, denyResult: String?) {
        if (status == AppStatusType.DENIED) requireDenyReason(denyResult)
        val release = getRelease(releaseId)
        release.updateStatus(status, denyResult, userId)
        release.app.updateReleaseInfo(enabled = release.enabled, status = release.status)
    }

    fun denyRelease(userId: UUID, releaseId: UUID, denyResult: String?) {
        updateReleaseStatus(userId, releaseId, AppStatusType.DENIED, denyResult)
    }

    fun toggleReleaseEnabled(userId: UUID, releaseId: UUID, enabled: Boolean) {
        val release = getReleaseWithOwnerPermission(userId, releaseId)
        if (enabled && release.status != AppStatusType.ALLOWED) {
            throw AppReleaseEnableNotAllowedException()
        }
        if (enabled) {
            appReleaseRepository.findAllByAppAndEnabledIsTrue(release.app)
                .filter { it.id != release.id }
                .forEach { it.updateEnabled(false, userId) }
        }
        release.updateEnabled(enabled, userId)
        release.app.updateReleaseInfo(enabled = release.enabled, status = release.status)
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

    fun updateApp(userId: UUID, command: EditAppCommand) {
        val app = getAppWithMemberPermission(userId, command.appId)
        command.name?.let {
            if (it != app.name && existByName(it)) throw AppAlreadyExistException()
        }
        app.update(command.name, command.subtitle, command.description, command.iconUrl, command.darkIconUrl, command.inquiryMail)
        command.server?.let { serverCommand ->
            val server = getAppServer(app)
            val wasEnabled = server.enabled
            server.updateServerInfo(
                name = serverCommand.name,
                serverAddress = serverCommand.serverAddress,
                redirectPath = serverCommand.redirectPath?.let { normalizeAndValidatePath(it) },
                prefixLevel = serverCommand.prefixLevel?.let { normalizePrefixLevel(it) },
                usePushNotification = serverCommand.usePushNotification
            )
            if (wasEnabled && !server.enabled) {
                appServerRouteEventProducer.publishUpdated(server)
            }
        }
    }

    fun updateServer(userId: UUID, command: EditServerCommand) {
        val app = getAppWithMemberPermission(userId, command.appId!!)
        val server = getAppServer(app)
        val wasEnabled = server.enabled
        server.updateServerInfo(
            name = command.name,
            serverAddress = command.serverAddress,
            redirectPath = command.redirectPath?.let { normalizeAndValidatePath(it) },
            prefixLevel = command.prefixLevel?.let { normalizePrefixLevel(it) },
            usePushNotification = command.usePushNotification
        )
        if (wasEnabled && !server.enabled) {
            appServerRouteEventProducer.publishUpdated(server)
        }
    }

    fun updateServerStatus(appId: UUID, status: AppStatusType, denyResult: String?) {
        if (status == AppStatusType.DENIED) requireDenyReason(denyResult)
        val app = getApp(appId)
        val server = getAppServer(app)
        val wasEnabled = server.enabled
        server.updateStatus(status, denyResult)
        if (status == AppStatusType.ALLOWED) {
            appServerRouteEventProducer.publishCreated(server)
            return
        }
        if (wasEnabled && !server.enabled) {
            appServerRouteEventProducer.publishUpdated(server)
        }
    }

    fun denyServer(appId: UUID, denyResult: String?) {
        updateServerStatus(appId, AppStatusType.DENIED, denyResult)
    }

    fun toggleServerEnabled(userId: UUID, appId: UUID, enabled: Boolean) {
        val app = getAppWithMemberPermission(userId, appId)
        val server = getAppServer(app)
        if (enabled && server.status != AppStatusType.ALLOWED) {
            throw AppServerEnableNotAllowedException()
        }
        if (server.enabled == enabled) return
        server.updateEnabled(enabled)
        appServerRouteEventProducer.publishUpdated(server)
    }

    fun createApiKey(userId: UUID, appId: UUID): AppApiKeyEntity {
        val app = getAppWithMemberPermission(userId, appId)
        if (appApiKeyRepository.existsByApp(app)) throw AppApiKeyAlreadyExistException()
        val rawKey = generateRawApiKey()
        val apiKeyEntity = appApiKeyRepository.save(
            AppApiKeyEntity(
                app = app,
                apiKey = passwordEncoder.encode(rawKey),
                expiredAt = LocalDateTime.now().plusDays(API_KEY_EXPIRE_DAYS),
                rawApiKey = rawKey,
            )
        )
        appApiKeyEventProducer.publishCreated(apiKeyEntity)
        return apiKeyEntity
    }

    fun regenerateApiKey(userId: UUID, appId: UUID): AppApiKeyEntity {
        val app = getAppWithMemberPermission(userId, appId)
        val apiKeyEntity = appApiKeyRepository.findByApp(app) ?: throw AppApiKeyNotFoundException()
        val rawKey = generateRawApiKey()
        apiKeyEntity.updateApiKey(passwordEncoder.encode(rawKey), LocalDateTime.now().plusDays(API_KEY_EXPIRE_DAYS))
        appApiKeyEventProducer.publishCreated(apiKeyEntity)
        return AppApiKeyEntity(app = apiKeyEntity.app, apiKey = apiKeyEntity.apiKey, expiredAt = apiKeyEntity.expiredAt, rawApiKey = rawKey)
    }

    fun verifyApiKey(appPublicId: UUID, rawApiKey: String): Boolean {
        val app = getApp(appPublicId)
        val apiKeyEntity = appApiKeyRepository.findByApp(app) ?: return false
        if (apiKeyEntity.isExpired) return false
        return passwordEncoder.matches(rawApiKey, apiKeyEntity.apiKey)
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

    private fun saveServer(app: AppEntity, command: CreateServerCommand) {
        appServerRepository.save(
            AppServerEntity(
                app = app,
                name = command.name,
                serverAddress = command.serverAddress,
                redirectPath = normalizeAndValidatePath(command.redirectPath),
                prefixLevel = normalizePrefixLevel(command.prefixLevel),
                usePushNotification = command.usePushNotification,
                enabled = false,
                status = AppStatusType.PENDING
            )
        )
    }

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

    private fun generateRawApiKey(): String {
        val random = SecureRandom()
        val bytes = ByteArray(32)
        random.nextBytes(bytes)
        return "dok_" + bytes.joinToString("") { "%02x".format(it) }
    }
}
