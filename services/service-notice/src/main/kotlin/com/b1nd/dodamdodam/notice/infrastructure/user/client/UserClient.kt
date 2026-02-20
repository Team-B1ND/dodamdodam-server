package com.b1nd.dodamdodam.notice.infrastructure.user.client

import com.b1nd.dodamdodam.grpc.user.GetUserInfoByUserIdRequest
import com.b1nd.dodamdodam.grpc.user.GetUserInfosByUserIdsRequest
import com.b1nd.dodamdodam.grpc.user.UserInfoServiceGrpcKt
import com.b1nd.dodamdodam.notice.application.notice.data.response.MemberInfoResponse
import com.b1nd.dodamdodam.notice.domain.notice.exception.UserNotFoundException
import com.b1nd.dodamdodam.notice.domain.notice.exception.UserServiceException
import com.b1nd.dodamdodam.notice.infrastructure.user.mapper.UserInfoMapper
import io.grpc.Status
import io.grpc.StatusException
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Component

@Component
class UserClient(
    private val userInfoMapper: UserInfoMapper
) {
    @GrpcClient("service-user")
    private lateinit var stub: UserInfoServiceGrpcKt.UserInfoServiceCoroutineStub

    suspend fun getUserInfoByUserId(userId: String): MemberInfoResponse? = runCatching {
        val request = GetUserInfoByUserIdRequest.newBuilder()
            .setUserId(userId)
            .build()

        userInfoMapper.toMemberInfoResponse(stub.getUserInfoByUserId(request))
    }.onFailure { ex ->
        if (ex is StatusException) {
            when (ex.status.code) {
                Status.Code.NOT_FOUND -> throw UserNotFoundException()
                else -> throw UserServiceException()
            }
        }
    }.getOrNull()

    suspend fun getUserInfosByUserIds(userIds: List<String>): Map<String, MemberInfoResponse> = runCatching {
        val request = GetUserInfosByUserIdsRequest.newBuilder()
            .addAllUserIds(userIds)
            .build()

        stub.getUserInfosByUserIds(request).usersList.associate { userInfo ->
            userInfo.userId to userInfoMapper.toMemberInfoResponse(userInfo)
        }
    }.onFailure { ex ->
        if (ex is StatusException) {
            when (ex.status.code) {
                Status.Code.NOT_FOUND -> throw UserNotFoundException()
                else -> throw UserServiceException()
            }
        }
    }.getOrDefault(emptyMap())
}
