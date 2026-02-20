package com.b1nd.dodamdodam.notice.infrastructure.user.client

import com.b1nd.dodamdodam.core.common.exception.base.BaseInternalServerException
import com.b1nd.dodamdodam.grpc.user.GetUserInfoByUsernameRequest
import com.b1nd.dodamdodam.grpc.user.GetUserInfosByUsernamesRequest
import com.b1nd.dodamdodam.grpc.user.UserInfoResponse
import com.b1nd.dodamdodam.grpc.user.UserInfoServiceGrpcKt
import com.b1nd.dodamdodam.notice.application.notice.data.response.MemberInfoResponse
import io.grpc.StatusException
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Component

@Component
class UserClient {
    @GrpcClient("service-user")
    private lateinit var stub: UserInfoServiceGrpcKt.UserInfoServiceCoroutineStub

    suspend fun getUserInfoByUsername(username: String): MemberInfoResponse? = runCatching {
        val request = GetUserInfoByUsernameRequest.newBuilder()
            .setUsername(username)
            .build()

        toMemberInfoResponse(stub.getUserInfoByUsername(request))
    }.onFailure { ex ->
        if (ex is StatusException) {
            throw BaseInternalServerException()
        }
    }.getOrNull()

    suspend fun getUserInfosByUsernames(usernames: List<String>): Map<String, MemberInfoResponse> = runCatching {
        val request = GetUserInfosByUsernamesRequest.newBuilder()
            .addAllUsernames(usernames)
            .build()

        stub.getUserInfosByUsernames(request).usersList.associate { userInfo ->
            userInfo.name to toMemberInfoResponse(userInfo)
        }
    }.onFailure { ex ->
        if (ex is StatusException) {
            throw BaseInternalServerException()
        }
    }.getOrDefault(emptyMap())

    private fun toMemberInfoResponse(userInfo: UserInfoResponse): MemberInfoResponse {
        return MemberInfoResponse(
            id = userInfo.userId,
            name = userInfo.name,
            profileImage = userInfo.profileImage.ifEmpty { null }
        )
    }
}
