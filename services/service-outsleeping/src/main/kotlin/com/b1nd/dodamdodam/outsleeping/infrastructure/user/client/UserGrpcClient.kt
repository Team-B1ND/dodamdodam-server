package com.b1nd.dodamdodam.outsleeping.infrastructure.user.client

import com.b1nd.dodamdodam.grpc.user.GetResidualStudentsRequest
import com.b1nd.dodamdodam.grpc.user.GetStudentsByUserIdsRequest
import com.b1nd.dodamdodam.grpc.user.ResidualStudentDto
import com.b1nd.dodamdodam.grpc.user.StudentDto
import com.b1nd.dodamdodam.grpc.user.UserQueryServiceGrpcKt
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.exception.OutSleepingStudentNotFoundException
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
        return students.firstOrNull() ?: throw OutSleepingStudentNotFoundException()
    }

    suspend fun getResidualStudents(absentUserIds: List<UUID>): List<ResidualStudentDto> {
        val request = GetResidualStudentsRequest.newBuilder()
            .addAllAbsentUserIds(absentUserIds.map { it.toString() })
            .build()
        return stub.getResidualStudents(request).studentsList
    }
}
