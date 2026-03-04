package com.b1nd.dodamdodam.nightstudy.application

import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.core.security.passport.holder.PassportHolder
import com.b1nd.dodamdodam.core.security.passport.requireUserId
import com.b1nd.dodamdodam.nightstudy.application.data.request.CreateNightStudyBanRequest
import com.b1nd.dodamdodam.nightstudy.application.data.response.NightStudyBanResponse
import com.b1nd.dodamdodam.nightstudy.application.data.toEntity
import com.b1nd.dodamdodam.nightstudy.application.data.toResponse
import com.b1nd.dodamdodam.nightstudy.domain.ban.service.NightStudyBanService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Component
@Transactional(rollbackFor = [Exception::class])
class NightStudyBanUseCase(
    private val nightStudyBanService: NightStudyBanService
) {

    fun create(request: CreateNightStudyBanRequest): Response<Any> {
        val entity = request.toEntity()
        nightStudyBanService.save(entity)
        return Response.created("심야자습 정지가 등록되었어요.")
    }

    @Transactional(readOnly = true)
    fun getByUserId(userId: UUID): Response<List<NightStudyBanResponse>> {
        val bans = nightStudyBanService.getActiveBansByUserId(userId).map { it.toResponse() }
        return Response.ok("심야자습 정지 정보를 조회했어요.", bans)
    }

    @Transactional(readOnly = true)
    fun getMy(): Response<List<NightStudyBanResponse>> {
        val userId = PassportHolder.current().requireUserId()
        val bans = nightStudyBanService.getActiveBansByUserId(userId).map { it.toResponse() }
        return Response.ok("내 심야자습 정지 정보를 조회했어요.", bans)
    }

    @Transactional(readOnly = true)
    fun getAll(): Response<List<NightStudyBanResponse>> {
        val bans = nightStudyBanService.getAll().map { it.toResponse() }
        return Response.ok("모든 심야자습 정지 정보를 조회했어요.", bans)
    }

    fun deleteByUserId(userId: UUID): Response<Any> {
        nightStudyBanService.deleteByUserId(userId)
        return Response.ok("심야자습 정지가 철회되었어요.")
    }
}
