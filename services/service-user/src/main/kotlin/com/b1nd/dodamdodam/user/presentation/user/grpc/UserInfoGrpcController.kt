package com.b1nd.dodamdodam.user.presentation.user.grpc

import com.b1nd.dodamdodam.core.common.coroutine.CoroutineBlockingExecutor
import com.b1nd.dodamdodam.grpc.user.GetUserInfoByUsernameRequest
import com.b1nd.dodamdodam.grpc.user.GetUserInfosByUsernamesRequest
import com.b1nd.dodamdodam.grpc.user.GetUserInfosByUsernamesResponse
import com.b1nd.dodamdodam.grpc.user.UserInfoResponse
import com.b1nd.dodamdodam.grpc.user.UserInfoServiceGrpcKt
import com.b1nd.dodamdodam.user.domain.user.entity.UserEntity
import com.b1nd.dodamdodam.user.domain.user.service.UserService
import net.devh.boot.grpc.server.service.GrpcService

@GrpcService
class UserInfoGrpcController(
    private val userService: UserService,
    private val blockingExecutor: CoroutineBlockingExecutor
): UserInfoServiceGrpcKt.UserInfoServiceCoroutineImplBase() {

    override suspend fun getUserInfoByUsername(request: GetUserInfoByUsernameRequest): UserInfoResponse {
        val user: UserEntity = blockingExecutor.execute {
            userService.getByUsername(request.username)
        }
        return toUserInfoResponse(user)
    }

    override suspend fun getUserInfosByUsernames(request: GetUserInfosByUsernamesRequest): GetUserInfosByUsernamesResponse {
        val users: List<UserEntity> = blockingExecutor.execute {
            userService.getByUsernames(request.usernamesList)
        }
        return GetUserInfosByUsernamesResponse.newBuilder()
            .addAllUsers(users.map { toUserInfoResponse(it) })
            .build()
    }

    private fun toUserInfoResponse(user: UserEntity): UserInfoResponse {
        return UserInfoResponse.newBuilder()
            .setUserId(user.publicId.toString())
            .setName(user.name)
            .setProfileImage(user.profileImage ?: "")
            .build()
    }
}
