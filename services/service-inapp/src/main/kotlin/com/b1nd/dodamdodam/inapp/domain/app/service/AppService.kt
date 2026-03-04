package com.b1nd.dodamdodam.inapp.domain.app.service

import com.b1nd.dodamdodam.inapp.domain.app.entity.AppEntity
import com.b1nd.dodamdodam.inapp.domain.app.entity.AppReleaseEntity
import com.b1nd.dodamdodam.inapp.domain.app.enumeration.AppReleaseStatusType
import com.b1nd.dodamdodam.inapp.domain.app.exception.AppAlreadyExistException
import com.b1nd.dodamdodam.inapp.domain.app.exception.AppNotFoundException
import com.b1nd.dodamdodam.inapp.domain.app.exception.AppReleaseEnableNotAllowedException
import com.b1nd.dodamdodam.inapp.domain.app.exception.AppReleaseNotFoundException
import com.b1nd.dodamdodam.inapp.domain.app.exception.AppTeamMemberPermissionRequiredException
import com.b1nd.dodamdodam.inapp.domain.app.exception.AppTeamOwnerPermissionRequiredException
import com.b1nd.dodamdodam.inapp.domain.app.repository.AppReleaseRepository
import com.b1nd.dodamdodam.inapp.domain.app.repository.AppRepository
import com.b1nd.dodamdodam.inapp.domain.team.entity.TeamEntity
import com.b1nd.dodamdodam.inapp.domain.team.repository.TeamMemberRepository
import com.b1nd.dodamdodam.inapp.domain.team.repository.TeamRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AppService(
    private val appRepository: AppRepository,
    private val appReleaseRepository: AppReleaseRepository,
    private val teamRepository: TeamRepository,
    private val teamMemberRepository: TeamMemberRepository
) {
    fun create(
        userId: UUID,
        teamId: UUID,
        name: String,
        subtitle: String,
        description: String?,
        iconUrl: String,
        darkIconUrl: String?,
        inquiryMail: String
    ): UUID {
        if (existByName(name)) throw AppAlreadyExistException()
        val team = getTeamWithMemberPermission(userId, teamId)
        val app = appRepository.save(AppEntity(name, description, subtitle, iconUrl, darkIconUrl, inquiryMail, team))
        return app.publicId!!
    }

    fun createRelease(
        userId: UUID,
        appId: UUID,
        releaseUrl: String,
        memo: String?
    ): UUID {
        val app = getApp(appId)
        validateAppMember(userId, app)
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
        val release = getRelease(releaseId)
        release.updateStatus(status, denyResult, userId)
    }

    fun toggleReleaseEnabled(userId: UUID, releaseId: UUID, enabled: Boolean) {
        val release = getRelease(releaseId)
        validateAppOwner(userId, release.app)
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
        val app = getApp(appId)
        validateAppOwner(userId, app)
        return appReleaseRepository.findAllByAppOrderByCreatedAtDesc(app)
    }

    fun getAppDetail(appId: UUID): Pair<AppEntity, List<AppReleaseEntity>> {
        val app = getApp(appId)
        val releases = appReleaseRepository.findAllByAppOrderByCreatedAtDesc(app)
        return app to releases
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
        val app = getApp(appId)
        validateAppMember(userId, app)
        name?.let {
            if (it != app.name && existByName(it)) throw AppAlreadyExistException()
        }
        app.update(name, subtitle, description, iconUrl, darkIconUrl, inquiryMail)
    }

    fun deleteApp(userId: UUID, appId: UUID) {
        val app = getApp(appId)
        validateAppOwner(userId, app)
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

    private fun getTeamWithOwnerPermission(userId: UUID, teamId: UUID): TeamEntity {
        val team = teamRepository.findByPublicId(teamId)
            ?: throw AppTeamOwnerPermissionRequiredException()
        if (!teamMemberRepository.existsByUserAndTeamAndIsOwnerIsTrue(userId, team)) {
            throw AppTeamOwnerPermissionRequiredException()
        }
        return team
    }

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
}
