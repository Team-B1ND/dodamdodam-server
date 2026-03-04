package com.b1nd.dodamdodam.nightstudy.application

import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.core.security.passport.holder.PassportHolder
import com.b1nd.dodamdodam.core.security.passport.requireUserId
import com.b1nd.dodamdodam.nightstudy.application.data.request.ApplyNightStudyProjectRequest
import com.b1nd.dodamdodam.nightstudy.application.data.request.RejectNightStudyRequest
import com.b1nd.dodamdodam.nightstudy.application.data.response.NightStudyProjectResponse
import com.b1nd.dodamdodam.nightstudy.application.data.response.ProjectRoomResponse
import com.b1nd.dodamdodam.nightstudy.application.data.toEntity
import com.b1nd.dodamdodam.nightstudy.application.data.toResponse
import com.b1nd.dodamdodam.nightstudy.application.data.toRoomResponse
import com.b1nd.dodamdodam.nightstudy.domain.ban.service.NightStudyBanService
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.exception.NightStudyBannedException
import com.b1nd.dodamdodam.nightstudy.domain.project.enumeration.ProjectRoom
import com.b1nd.dodamdodam.nightstudy.domain.project.exception.NightStudyProjectNotOwnerException
import com.b1nd.dodamdodam.nightstudy.domain.project.service.NightStudyProjectService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.UUID

@Component
@Transactional(rollbackFor = [Exception::class])
class NightStudyProjectUseCase(
    private val nightStudyProjectService: NightStudyProjectService,
    private val nightStudyBanService: NightStudyBanService
) {

    fun apply(request: ApplyNightStudyProjectRequest): Response<Any> {
        val userId = PassportHolder.current().requireUserId()
        if (nightStudyBanService.isBanned(userId)) {
            throw NightStudyBannedException()
        }
        val entity = request.toEntity(userId)
        nightStudyProjectService.save(entity)
        return Response.created("프로젝트 심야자습이 신청되었어요.")
    }

    @Transactional(readOnly = true)
    fun getMy(): Response<List<NightStudyProjectResponse>> {
        val userId = PassportHolder.current().requireUserId()
        val projects = nightStudyProjectService.getByUserId(userId).map { it.toResponse() }
        return Response.ok("내 프로젝트 심야자습을 조회했어요.", projects)
    }

    @Transactional(readOnly = true)
    fun getValid(): Response<List<NightStudyProjectResponse>> {
        val projects = nightStudyProjectService.getAllowed(LocalDate.now()).map { it.toResponse() }
        return Response.ok("유효한 프로젝트 심야자습을 조회했어요.", projects)
    }

    @Transactional(readOnly = true)
    fun getPending(): Response<List<NightStudyProjectResponse>> {
        val projects = nightStudyProjectService.getPending().map { it.toResponse() }
        return Response.ok("대기 중인 프로젝트 심야자습을 조회했어요.", projects)
    }

    @Transactional(readOnly = true)
    fun getAllowed(): Response<List<NightStudyProjectResponse>> {
        val projects = nightStudyProjectService.getAllowed(LocalDate.now()).map { it.toResponse() }
        return Response.ok("승인된 프로젝트 심야자습을 조회했어요.", projects)
    }

    @Transactional(readOnly = true)
    fun getRooms(): Response<List<ProjectRoomResponse>> {
        val projects = nightStudyProjectService.getAllowed(LocalDate.now())
        val rooms = projects.filter { it.room != null }.map { it.toRoomResponse() }
        return Response.ok("사용 중인 실을 조회했어요.", rooms)
    }

    @Transactional(readOnly = true)
    fun getStudents(): Response<List<UUID>> {
        val projects = nightStudyProjectService.getAllowed(LocalDate.now())
        val studentIds = projects.flatMap { project ->
            project.members.map { it.userId }
        }.distinct()
        return Response.ok("오늘 프로젝트 참가 학생을 조회했어요.", studentIds)
    }

    @Transactional(readOnly = true)
    fun getById(id: Long): Response<NightStudyProjectResponse> {
        val project = nightStudyProjectService.getById(id).toResponse()
        return Response.ok("프로젝트 심야자습을 조회했어요.", project)
    }

    fun allow(id: Long, room: ProjectRoom): Response<Any> {
        val project = nightStudyProjectService.getById(id)
        project.allow(room)
        nightStudyProjectService.save(project)
        return Response.ok("프로젝트 심야자습이 승인되었어요.")
    }

    fun reject(id: Long, request: RejectNightStudyRequest): Response<Any> {
        val project = nightStudyProjectService.getById(id)
        project.reject(request.rejectReason)
        nightStudyProjectService.save(project)
        return Response.ok("프로젝트 심야자습이 거절되었어요.")
    }

    fun revert(id: Long): Response<Any> {
        val project = nightStudyProjectService.getById(id)
        project.revert()
        nightStudyProjectService.save(project)
        return Response.ok("프로젝트 심야자습이 대기로 되돌려졌어요.")
    }

    fun delete(id: Long): Response<Any> {
        val userId = PassportHolder.current().requireUserId()
        val project = nightStudyProjectService.getById(id)
        if (project.userId != userId) {
            throw NightStudyProjectNotOwnerException()
        }
        nightStudyProjectService.delete(project)
        return Response.ok("프로젝트 심야자습이 삭제되었어요.")
    }
}
