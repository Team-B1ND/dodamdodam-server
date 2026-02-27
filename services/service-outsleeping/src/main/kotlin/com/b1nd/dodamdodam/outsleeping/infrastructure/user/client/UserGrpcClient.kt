package com.b1nd.dodamdodam.outsleeping.infrastructure.user.client

import com.b1nd.dodamdodam.grpc.user.GetResidualStudentsRequest
import com.b1nd.dodamdodam.grpc.user.GetStudentsByUserIdsRequest
import com.b1nd.dodamdodam.grpc.user.ResidualStudent
import com.b1nd.dodamdodam.grpc.user.Student
import com.b1nd.dodamdodam.grpc.user.UserQueryServiceGrpcKt
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.exception.OutSleepingStudentNotFoundException
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UserGrpcClient {

    @GrpcClient("service-user")
    private lateinit var stub: UserQueryServiceGrpcKt.UserQueryServiceCoroutineStub

    suspend fun getStudentsByUserIds(userIds: List<UUID>): List<Student> {
        if (userIds.isEmpty()) return emptyList()
        val request = GetStudentsByUserIdsRequest.newBuilder()
            .addAllUserIds(userIds.map { it.toString() })
            .build()
        return stub.getStudentsByUserIds(request).studentsList
    }

    suspend fun getStudentByUserId(userId: UUID): Student {
        val students = getStudentsByUserIds(listOf(userId))
        return students.firstOrNull() ?: throw OutSleepingStudentNotFoundException()
    }

    suspend fun getResidualStudents(absentUserIds: List<UUID>): List<ResidualStudent> {
        val request = GetResidualStudentsRequest.newBuilder()
            .addAllAbsentUserIds(absentUserIds.map { it.toString() })
            .build()
        return stub.getResidualStudents(request).studentsList
    }
}
