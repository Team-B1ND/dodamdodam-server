package com.b1nd.dodamdodam.club.application.club

import com.b1nd.dodamdodam.club.application.club.data.request.CreateClubRequest
import com.b1nd.dodamdodam.club.application.club.data.request.UpdateClubRequest
import com.b1nd.dodamdodam.club.application.club.data.response.ClubResponse
import com.b1nd.dodamdodam.club.application.club.data.toEntity
import com.b1nd.dodamdodam.club.domain.club.service.ClubService
import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.core.security.passport.holder.PassportHolder
import com.b1nd.dodamdodam.core.security.passport.requireUserId
import jakarta.transaction.Transactional
import org.springframework.stereotype.Component
import java.util.UUID

@Component
@Transactional(rollbackOn = [Exception::class])
class ClubUseCase(
    private val clubService: ClubService,
) {
    fun createClub(request: CreateClubRequest): Response<Any> {
        val userId = PassportHolder.current().requireUserId()
        clubService.create(userId, request.toEntity())
        return Response.created("동아리가 생성되었어요.")
    }

    fun getClub(publicId: UUID): Response<ClubResponse> {
        val club = clubService.get(publicId)
        return Response.ok("동아리를 조회했어요.", ClubResponse.fromEntity(club))
    }

    fun getClubs(): Response<List<ClubResponse>> {
        val clubs = clubService.getAll().map { ClubResponse.fromEntity(it) }
        return Response.ok("동아리 목록을 조회했어요.", clubs)
    }

    fun updateClub(publicId: UUID, request: UpdateClubRequest): Response<ClubResponse> {
        val userId = PassportHolder.current().requireUserId()
        val updatedClub = clubService.update(publicId, userId, request.name, request.description, request.imageUrl, request.category, request.type)
        return Response.ok("동아리가 수정되었어요.", ClubResponse.fromEntity(updatedClub))
    }

    fun deleteClub(publicId: UUID): Response<Any> {
        val userId = PassportHolder.current().requireUserId()
        clubService.delete(publicId, userId)
        return Response.ok("동아리가 삭제되었어요.")
    }
}
