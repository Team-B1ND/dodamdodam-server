package com.b1nd.dodamdodam.nightstudy.application

import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.core.security.passport.holder.PassportHolder
import com.b1nd.dodamdodam.core.security.passport.requireUserId
import com.b1nd.dodamdodam.nightstudy.application.data.request.ApplyNightStudyRequest
import com.b1nd.dodamdodam.nightstudy.application.data.request.RejectNightStudyRequest
import com.b1nd.dodamdodam.nightstudy.application.data.response.CombinedNightStudyResponse
import com.b1nd.dodamdodam.nightstudy.application.data.response.NightStudyResponse
import com.b1nd.dodamdodam.nightstudy.application.data.toEntity
import com.b1nd.dodamdodam.nightstudy.application.data.toProjectResponse
import com.b1nd.dodamdodam.nightstudy.application.data.toResponse
import com.b1nd.dodamdodam.nightstudy.domain.ban.service.NightStudyBanService
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.exception.NightStudyBannedException
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.exception.NightStudyNotOwnerException
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.service.NightStudyService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Component
@Transactional(rollbackFor = [Exception::class])
class NightStudyUseCase(
    private val nightStudyService: NightStudyService,
    private val nightStudyBanService: NightStudyBanService
) {

    fun apply(request: ApplyNightStudyRequest): Response<Any> {
        val userId = PassportHolder.current().requireUserId()
        if (nightStudyBanService.isBanned(userId)) {
            throw NightStudyBannedException()
        }
        val entity = request.toEntity(userId)
        nightStudyService.save(entity)
        return Response.created("심야자습이 신청되었어요.")
    }

    @Transactional(readOnly = true)
    fun getMy(): Response<List<NightStudyResponse>> {
        val userId = PassportHolder.current().requireUserId()
        val studies = nightStudyService.getByUserId(userId).map { it.toResponse() }
        return Response.ok("내 심야자습을 조회했어요.", studies)
    }

    @Transactional(readOnly = true)
    fun getAllowed(): Response<List<NightStudyResponse>> {
        val studies = nightStudyService.getAllowed(LocalDate.now()).map { it.toResponse() }
        return Response.ok("승인된 심야자습을 조회했어요.", studies)
    }

    @Transactional(readOnly = true)
    fun getAll(): Response<List<NightStudyResponse>> {
        val studies = nightStudyService.getAll().map { it.toResponse() }
        return Response.ok("모든 심야자습을 조회했어요.", studies)
    }

    @Transactional(readOnly = true)
    fun getPending(): Response<List<NightStudyResponse>> {
        val studies = nightStudyService.getPending().map { it.toResponse() }
        return Response.ok("대기 중인 심야자습을 조회했어요.", studies)
    }

    fun allow(id: Long): Response<Any> {
        val nightStudy = nightStudyService.getById(id)
        nightStudy.allow()
        nightStudyService.save(nightStudy)
        return Response.ok("심야자습이 승인되었어요.")
    }

    fun reject(id: Long, request: RejectNightStudyRequest): Response<Any> {
        val nightStudy = nightStudyService.getById(id)
        nightStudy.reject(request.rejectReason)
        nightStudyService.save(nightStudy)
        return Response.ok("심야자습이 거절되었어요.")
    }

    fun revert(id: Long): Response<Any> {
        val nightStudy = nightStudyService.getById(id)
        nightStudy.revert()
        nightStudyService.save(nightStudy)
        return Response.ok("심야자습이 대기로 되돌려졌어요.")
    }

    fun delete(id: Long): Response<Any> {
        val userId = PassportHolder.current().requireUserId()
        val nightStudy = nightStudyService.getById(id)
        if (nightStudy.userId != userId) {
            throw NightStudyNotOwnerException()
        }
        nightStudyService.softDelete(nightStudy)
        return Response.ok("심야자습이 삭제되었어요.")
    }

    @Transactional(readOnly = true)
    fun getCombined(): Response<CombinedNightStudyResponse> {
        val today = LocalDate.now()
        val nightStudies = nightStudyService.getAllowed(today).map { it.toResponse() }
        val projectEntities = nightStudyService.getAllowedProjects(today)
        val nightStudyIds = projectEntities.mapNotNull { it.id }
        val membersMap = nightStudyService.getMembersByNightStudyIds(nightStudyIds)
            .groupBy { it.nightStudyId }
        val projects = projectEntities.map { project ->
            val memberUserIds = membersMap[project.id]?.map { it.userId } ?: emptyList()
            project.toProjectResponse(memberUserIds)
        }
        val combined = CombinedNightStudyResponse(
            nightStudies = nightStudies,
            projects = projects
        )
        return Response.ok("심야자습 통합 정보를 조회했어요.", combined)
    }
}
