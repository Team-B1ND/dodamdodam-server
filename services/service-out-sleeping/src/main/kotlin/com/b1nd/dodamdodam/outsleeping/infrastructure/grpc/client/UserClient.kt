package com.b1nd.dodamdodam.outsleeping.infrastructure.grpc.client

import com.b1nd.dodamdodam.grpc.user.GetAllStudentsRequest
import com.b1nd.dodamdodam.grpc.user.GetUserInfoRequest
import com.b1nd.dodamdodam.grpc.user.GetUserInfosByIdsRequest
import com.b1nd.dodamdodam.grpc.user.UserInfoMessage
import com.b1nd.dodamdodam.grpc.user.UserInfoServiceGrpcKt
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UserClient {
    @GrpcClient("service-user")
    private lateinit var stub: UserInfoServiceGrpcKt.UserInfoServiceCoroutineStub

    suspend fun getUserInfo(userId: UUID): UserInfoMessage {
        val request = GetUserInfoRequest.newBuilder()
            .setPublicId(userId.toString())
            .build()
        return stub.getUserInfo(request).user
    }

    suspend fun getUserInfosByIds(userIds: Collection<UUID>): List<UserInfoMessage> {
        val request = GetUserInfosByIdsRequest.newBuilder()
            .addAllPublicIds(userIds.map { it.toString() })
            .build()
        return stub.getUserInfosByIds(request).usersList
    }

    suspend fun getAllStudents(): List<UserInfoMessage> {
        val request = GetAllStudentsRequest.newBuilder().build()
        return stub.getAllStudents(request).usersList
    }
}
