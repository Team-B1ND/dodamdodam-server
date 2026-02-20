package com.b1nd.dodamdodam.wakeupsong.infrastructure.user.client

import com.b1nd.dodamdodam.grpc.user.GetStudentsByUserIdsRequest
import com.b1nd.dodamdodam.grpc.user.StudentDto
import com.b1nd.dodamdodam.grpc.user.UserQueryServiceGrpcKt
import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.exception.WakeupSongStudentNotFoundException
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UserGrpcClient {

    @GrpcClient("service-user")
    private lateinit var stub: UserQueryServiceGrpcKt.UserQueryServiceCoroutineStub

    suspend fun getStudentByUserId(userId: UUID): StudentDto {
        val request = GetStudentsByUserIdsRequest.newBuilder()
            .addAllUserIds(listOf(userId.toString()))
            .build()
        return stub.getStudentsByUserIds(request).studentsList.firstOrNull()
            ?: throw WakeupSongStudentNotFoundException()
    }
}
