package com.b1nd.dodamdodam.outgoing.infrastructure.user.client

import com.b1nd.dodamdodam.grpc.user.GetStudentsByUserIdsRequest
import com.b1nd.dodamdodam.grpc.user.StudentDto
import com.b1nd.dodamdodam.grpc.user.UserQueryServiceGrpcKt
import com.b1nd.dodamdodam.outgoing.domain.outgoing.exception.OutGoingStudentNotFoundException
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UserGrpcClient {

    @GrpcClient("service-user")
    private lateinit var stub: UserQueryServiceGrpcKt.UserQueryServiceCoroutineStub

    suspend fun getStudentsByUserIds(userIds: List<UUID>): List<StudentDto> {
        if (userIds.isEmpty()) return emptyList()
        val request = GetStudentsByUserIdsRequest.newBuilder()
            .addAllUserIds(userIds.map { it.toString() })
            .build()
        return stub.getStudentsByUserIds(request).studentsList
    }

    suspend fun getStudentByUserId(userId: UUID): StudentDto {
        val students = getStudentsByUserIds(listOf(userId))
        return students.firstOrNull() ?: throw OutGoingStudentNotFoundException()
    }
}
