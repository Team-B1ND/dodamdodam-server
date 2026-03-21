package com.b1nd.dodamdodam.outsleeping.infrastructure.grpc.client

import com.b1nd.dodamdodam.core.common.exception.base.BaseInternalServerException
import com.b1nd.dodamdodam.grpc.user.GetAllStudentsRequest
import com.b1nd.dodamdodam.grpc.user.GetUserInfoRequest
import com.b1nd.dodamdodam.grpc.user.GetUserInfosByIdsRequest
import com.b1nd.dodamdodam.grpc.user.UserInfoMessage
import com.b1nd.dodamdodam.grpc.user.UserInfoServiceGrpcKt
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.exception.OutSleepingNotFoundException
import io.grpc.Status
import io.grpc.StatusException
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UserClient {
    @GrpcClient("service-user")
    private lateinit var stub: UserInfoServiceGrpcKt.UserInfoServiceCoroutineStub

    suspend fun getUserInfo(userId: UUID): UserInfoMessage = runCatching {
        val request = GetUserInfoRequest.newBuilder()
            .setPublicId(userId.toString())
            .build()
        stub.getUserInfo(request).user
    }.onFailure { ex ->
        if (ex is StatusException) {
            throw when (ex.status.code) {
                Status.Code.NOT_FOUND -> OutSleepingNotFoundException()
                else -> BaseInternalServerException()
            }
        }
    }.getOrThrow()

    suspend fun getUserInfosByIds(userIds: Collection<UUID>): List<UserInfoMessage> = runCatching {
        val request = GetUserInfosByIdsRequest.newBuilder()
            .addAllPublicIds(userIds.map { it.toString() })
            .build()
        stub.getUserInfosByIds(request).usersList
    }.onFailure { ex ->
        if (ex is StatusException) {
            throw when (ex.status.code) {
                Status.Code.NOT_FOUND -> OutSleepingNotFoundException()
                else -> BaseInternalServerException()
            }
        }
    }.getOrThrow()

    suspend fun getAllStudents(): List<UserInfoMessage> = runCatching {
        val request = GetAllStudentsRequest.newBuilder().build()
        stub.getAllStudents(request).usersList
    }.onFailure { ex ->
        if (ex is StatusException) {
            throw BaseInternalServerException()
        }
    }.getOrThrow()
}
