package com.b1nd.dodamdodam.nightstudy.application.nightstudy

import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.core.security.passport.holder.PassportHolder
import com.b1nd.dodamdodam.core.security.passport.requireUserId
import com.b1nd.dodamdodam.nightstudy.application.nightstudy.data.request.ApplyPersonalNightStudyRequest
import com.b1nd.dodamdodam.nightstudy.application.nightstudy.data.request.ApplyProjectNightStudyRequest
import com.b1nd.dodamdodam.nightstudy.application.nightstudy.data.response.PersonalNightStudyResponse
import com.b1nd.dodamdodam.nightstudy.application.nightstudy.data.response.ProjectNightStudyResponse
import com.b1nd.dodamdodam.nightstudy.application.nightstudy.data.toEntity
import com.b1nd.dodamdodam.nightstudy.application.nightstudy.data.toPersonalNightStudyListResponse
import com.b1nd.dodamdodam.nightstudy.application.nightstudy.data.toProjectNightStudyListResponse
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.enumeration.NightStudyStatusType
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.enumeration.NightStudyType
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.service.NightStudyService
import com.b1nd.dodamdodam.nightstudy.infrastructure.user.client.UserQueryClient
import jakarta.transaction.Transactional
import org.springframework.stereotype.Component

@Component
@Transactional(rollbackOn = [Exception::class])
class NightStudyUseCase (
    private val nightStudyService: NightStudyService,
    private val userQueryClient: UserQueryClient
) {
    fun applyPersonalNightStudy(request: ApplyPersonalNightStudyRequest): Response<Any> {
        val userId = PassportHolder.current().requireUserId()
        nightStudyService.save(request.toEntity(userId), null)
        return Response.created("개인 심자 신청이 완료됐어요.")
    }

    fun applyProjectNightStudy(request: ApplyProjectNightStudyRequest): Response<Any> {
        val userId = PassportHolder.current().requireUserId()
        nightStudyService.save(request.toEntity(userId), request.members)
        return Response.created("프로젝트 심자 신청이 완료됐어요.")
    }

    fun getMyPersonalNightStudy(status: NightStudyStatusType): Response<List<PersonalNightStudyResponse>> {
        val userId = PassportHolder.current().requireUserId()
        val result = nightStudyService.findAllByUserIdAndStatusAndType(userId, status, NightStudyType.PERSONAL)
        return Response.ok("개인 심자 신청 목록을 조회했어요.", result.toPersonalNightStudyListResponse())
    }

    fun getMyProjectNightStudy(status: NightStudyStatusType): Response<List<ProjectNightStudyResponse>> {
        val userId = PassportHolder.current().requireUserId()
        val result = nightStudyService.findAllByUserIdAndStatusAndType(userId, status, NightStudyType.PROJECT)
        return Response.ok("프로젝트 심자 신청 목록을 조회했어요.", result.toProjectNightStudyListResponse())
    }

    fun cancelNightStudy(id: Long) {
        var userId = PassportHolder.current().requireUserId()
        nightStudyService.delete(id)
    }
}