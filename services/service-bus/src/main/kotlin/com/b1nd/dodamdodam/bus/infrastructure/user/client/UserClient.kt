package com.b1nd.dodamdodam.bus.infrastructure.user.client

import com.b1nd.dodamdodam.core.common.exception.base.BaseInternalServerException
import com.b1nd.dodamdodam.grpc.user.GetUserInfosByUsernamesRequest
import com.b1nd.dodamdodam.grpc.user.UserInfoResponse
import com.b1nd.dodamdodam.grpc.user.UserInfoServiceGrpcKt
import io.grpc.StatusException
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Component

@Component
class UserClient {
    @GrpcClient("service-user")
    private lateinit var stub: UserInfoServiceGrpcKt.UserInfoServiceCoroutineStub

    suspend fun getUserInfosByUsernames(usernames: List<String>): Map<String, UserInfoResponse> = runCatching {
        val request = GetUserInfosByUsernamesRequest.newBuilder()
            .addAllUsernames(usernames)
            .build()

        stub.getUserInfosByUsernames(request).usersList.associateBy { it.username }
    }.onFailure { ex ->
        if (ex is StatusException) {
            throw BaseInternalServerException()
        }
    }.getOrDefault(emptyMap())
}
