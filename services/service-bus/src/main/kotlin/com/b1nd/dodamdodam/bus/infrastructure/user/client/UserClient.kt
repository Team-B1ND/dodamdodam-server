package com.b1nd.dodamdodam.bus.infrastructure.user.client

import com.b1nd.dodamdodam.bus.domain.bus.exception.UserNotFoundException
import com.b1nd.dodamdodam.bus.domain.bus.exception.UserServiceException
import com.b1nd.dodamdodam.grpc.user.GetUserInfosByUserIdsRequest
import com.b1nd.dodamdodam.grpc.user.UserInfoResponse
import com.b1nd.dodamdodam.grpc.user.UserInfoServiceGrpcKt
import io.grpc.Status
import io.grpc.StatusException
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Component

@Component
class UserClient {
    @GrpcClient("service-user")
    private lateinit var stub: UserInfoServiceGrpcKt.UserInfoServiceCoroutineStub

    suspend fun getUserInfosByUserIds(userIds: List<String>): Map<String, UserInfoResponse> = runCatching {
        val request = GetUserInfosByUserIdsRequest.newBuilder()
            .addAllUserIds(userIds)
            .build()

        stub.getUserInfosByUserIds(request).usersList.associateBy { it.userId }
    }.onFailure { ex ->
        if (ex is StatusException) {
            when (ex.status.code) {
                Status.Code.NOT_FOUND -> throw UserNotFoundException()
                else -> throw UserServiceException()
            }
        }
    }.getOrDefault(emptyMap())
}
