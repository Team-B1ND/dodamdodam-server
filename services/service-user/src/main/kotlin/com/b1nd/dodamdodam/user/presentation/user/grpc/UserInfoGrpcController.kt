package com.b1nd.dodamdodam.user.presentation.user.grpc

import com.b1nd.dodamdodam.core.common.coroutine.CoroutineBlockingExecutor
import com.b1nd.dodamdodam.grpc.user.GetUserInfoByUserIdRequest
import com.b1nd.dodamdodam.grpc.user.GetUserInfosByUserIdsRequest
import com.b1nd.dodamdodam.grpc.user.GetUserInfosByUserIdsResponse
import com.b1nd.dodamdodam.grpc.user.UserInfoResponse
import com.b1nd.dodamdodam.grpc.user.UserInfoServiceGrpcKt
import com.b1nd.dodamdodam.user.domain.user.entity.UserEntity
import com.b1nd.dodamdodam.user.domain.user.service.UserService
import net.devh.boot.grpc.server.service.GrpcService
import java.util.UUID

@GrpcService
class UserInfoGrpcController(
    private val userService: UserService,
    private val blockingExecutor: CoroutineBlockingExecutor
): UserInfoServiceGrpcKt.UserInfoServiceCoroutineImplBase() {

    override suspend fun getUserInfoByUserId(request: GetUserInfoByUserIdRequest): UserInfoResponse {
        val user: UserEntity = blockingExecutor.execute {
            userService.getByPublicId(UUID.fromString(request.userId))
        }
        return buildUserInfoResponse(user)
    }

    override suspend fun getUserInfosByUserIds(request: GetUserInfosByUserIdsRequest): GetUserInfosByUserIdsResponse {
        val users: List<UserEntity> = blockingExecutor.execute {
            userService.getByPublicIds(request.userIdsList.map { UUID.fromString(it) })
        }
        return GetUserInfosByUserIdsResponse.newBuilder()
            .addAllUsers(users.map { buildUserInfoResponse(it) })
            .build()
    }

    private fun buildUserInfoResponse(user: UserEntity): UserInfoResponse {
        return UserInfoResponse.newBuilder()
            .setUserId(user.publicId.toString())
            .setName(user.name)
            .setProfileImage(user.profileImage ?: "")
            .build()
    }
}
