package com.b1nd.dodamdodam.nightstudy.application

import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.core.security.passport.holder.PassportHolder
import com.b1nd.dodamdodam.core.security.passport.requireUserId
import com.b1nd.dodamdodam.nightstudy.application.data.request.ApplyNightStudyProjectRequest
import com.b1nd.dodamdodam.nightstudy.application.data.request.RejectNightStudyRequest
import com.b1nd.dodamdodam.nightstudy.application.data.response.NightStudyProjectResponse
import com.b1nd.dodamdodam.nightstudy.application.data.response.ProjectRoomResponse
import com.b1nd.dodamdodam.nightstudy.application.data.toEntity
import com.b1nd.dodamdodam.nightstudy.application.data.toMemberEntities
import com.b1nd.dodamdodam.nightstudy.application.data.toProjectResponse
import com.b1nd.dodamdodam.nightstudy.application.data.toRoomResponse
import com.b1nd.dodamdodam.nightstudy.domain.ban.service.NightStudyBanService
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.entity.NightStudyEntity
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.enumeration.ProjectRoom
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.exception.NightStudyBannedException
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.exception.NightStudyNotOwnerException
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.service.NightStudyService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.UUID

@Component
@Transactional(rollbackFor = [Exception::class])
class NightStudyProjectUseCase(
    private val nightStudyService: NightStudyService,
    private val nightStudyBanService: NightStudyBanService
) {

    fun apply(request: ApplyNightStudyProjectRequest): Response<Any> {
        val userId = PassportHolder.current().requireUserId()
        if (nightStudyBanService.isBanned(userId)) {
            throw NightStudyBannedException()
        }
        val entity = request.toEntity(userId)
        val saved = nightStudyService.save(entity)
        val members = request.toMemberEntities(saved.id!!)
        nightStudyService.saveMembers(members)
        return Response.created("프로젝트 심야자습이 신청되었어요.")
    }

    @Transactional(readOnly = true)
    fun getMy(): Response<List<NightStudyProjectResponse>> {
        val userId = PassportHolder.current().requireUserId()
        val projects = nightStudyService.getProjectsByUserId(userId)
        return Response.ok("내 프로젝트 심야자습을 조회했어요.", toProjectResponses(projects))
    }

    @Transactional(readOnly = true)
    fun getValid(): Response<List<NightStudyProjectResponse>> {
        val projects = nightStudyService.getAllowedProjects(LocalDate.now())
        return Response.ok("유효한 프로젝트 심야자습을 조회했어요.", toProjectResponses(projects))
    }

    @Transactional(readOnly = true)
    fun getPending(): Response<List<NightStudyProjectResponse>> {
        val projects = nightStudyService.getPendingProjects()
        return Response.ok("대기 중인 프로젝트 심야자습을 조회했어요.", toProjectResponses(projects))
    }

    @Transactional(readOnly = true)
    fun getAllowed(): Response<List<NightStudyProjectResponse>> {
        val projects = nightStudyService.getAllowedProjects(LocalDate.now())
        return Response.ok("승인된 프로젝트 심야자습을 조회했어요.", toProjectResponses(projects))
    }

    @Transactional(readOnly = true)
    fun getRooms(): Response<List<ProjectRoomResponse>> {
        val projects = nightStudyService.getAllowedProjects(LocalDate.now())
        val rooms = projects.filter { it.room != null }.map { it.toRoomResponse() }
        return Response.ok("사용 중인 실을 조회했어요.", rooms)
    }

    @Transactional(readOnly = true)
    fun getStudents(): Response<List<UUID>> {
        val projects = nightStudyService.getAllowedProjects(LocalDate.now())
        val nightStudyIds = projects.mapNotNull { it.id }
        val members = nightStudyService.getMembersByNightStudyIds(nightStudyIds)
        val studentIds = members.map { it.userId }.distinct()
        return Response.ok("오늘 프로젝트 참가 학생을 조회했어요.", studentIds)
    }

    @Transactional(readOnly = true)
    fun getById(id: Long): Response<NightStudyProjectResponse> {
        val project = nightStudyService.getById(id)
        val memberUserIds = nightStudyService.getMembersByNightStudyId(id).map { it.userId }
        return Response.ok("프로젝트 심야자습을 조회했어요.", project.toProjectResponse(memberUserIds))
    }

    fun allow(id: Long, room: ProjectRoom): Response<Any> {
        val project = nightStudyService.getById(id)
        project.allow(room)
        nightStudyService.save(project)
        return Response.ok("프로젝트 심야자습이 승인되었어요.")
    }

    fun reject(id: Long, request: RejectNightStudyRequest): Response<Any> {
        val project = nightStudyService.getById(id)
        project.reject(request.rejectReason)
        nightStudyService.save(project)
        return Response.ok("프로젝트 심야자습이 거절되었어요.")
    }

    fun revert(id: Long): Response<Any> {
        val project = nightStudyService.getById(id)
        project.revert()
        nightStudyService.save(project)
        return Response.ok("프로젝트 심야자습이 대기로 되돌려졌어요.")
    }

    fun delete(id: Long): Response<Any> {
        val userId = PassportHolder.current().requireUserId()
        val project = nightStudyService.getById(id)
        if (project.userId != userId) {
            throw NightStudyNotOwnerException()
        }
        nightStudyService.softDelete(project)
        return Response.ok("프로젝트 심야자습이 삭제되었어요.")
    }

    private fun toProjectResponses(projects: List<NightStudyEntity>): List<NightStudyProjectResponse> {
        val nightStudyIds = projects.mapNotNull { it.id }
        val membersMap = nightStudyService.getMembersByNightStudyIds(nightStudyIds)
            .groupBy { it.nightStudyId }
        return projects.map { project ->
            val memberUserIds = membersMap[project.id]?.map { it.userId } ?: emptyList()
            project.toProjectResponse(memberUserIds)
        }
    }
}
