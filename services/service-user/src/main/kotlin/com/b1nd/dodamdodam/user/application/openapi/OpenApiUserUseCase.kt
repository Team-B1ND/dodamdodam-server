package com.b1nd.dodamdodam.user.application.openapi

import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.user.application.openapi.data.request.GetUsersByPublicIdsRequest
import com.b1nd.dodamdodam.user.application.openapi.data.toUserInfoResponse
import com.b1nd.dodamdodam.user.application.user.data.response.UserInfoResponse
import com.b1nd.dodamdodam.user.domain.user.data.UserWithDetails
import com.b1nd.dodamdodam.user.domain.user.service.OpenApiUserService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Component
import java.util.UUID

@Component
@Transactional(rollbackOn = [Exception::class])
class OpenApiUserUseCase(
    private val openApiUserService: OpenApiUserService,
) {
    fun getUser(publicId: UUID): UserWithDetails? {
        return openApiUserService.getUsersWithDetails(listOf(publicId)).firstOrNull()
    }

    fun getUsers(publicIds: List<UUID>): List<UserWithDetails> {
        return openApiUserService.getUsersWithDetails(publicIds)
    }

    fun getUsersResponse(request: GetUsersByPublicIdsRequest): Response<List<UserInfoResponse>> {
        val users = openApiUserService.getUsersWithDetails(request.userIds)
        return Response.ok("유저 정보를 조회했어요.", users.map { it.toUserInfoResponse() })
    }
}