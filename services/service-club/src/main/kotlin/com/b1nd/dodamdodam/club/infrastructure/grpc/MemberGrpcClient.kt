package com.b1nd.dodamdodam.club.infrastructure.grpc

import com.b1nd.dodamdodam.grpc.member.GetStudentByUsernameRequest
import com.b1nd.dodamdodam.grpc.member.GetStudentsByIdsRequest
import com.b1nd.dodamdodam.grpc.member.GetTeacherByIdRequest
import com.b1nd.dodamdodam.grpc.member.MemberQueryServiceGrpcKt
import com.b1nd.dodamdodam.grpc.member.StudentInfo
import com.b1nd.dodamdodam.grpc.member.TeacherInfo
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Component

@Component
class MemberGrpcClient {
    @GrpcClient("service-member")
    private lateinit var stub: MemberQueryServiceGrpcKt.MemberQueryServiceCoroutineStub

    suspend fun getStudentsByIds(studentIds: List<Long>): List<StudentInfo> = runCatching {
        val request = GetStudentsByIdsRequest.newBuilder()
            .addAllStudentIds(studentIds)
            .build()

        stub.getStudentsByIds(request).studentsList
    }.getOrDefault(emptyList())

    suspend fun getTeacherById(teacherId: Long): TeacherInfo? = runCatching {
        val request = GetTeacherByIdRequest.newBuilder()
            .setTeacherId(teacherId)
            .build()

        stub.getTeacherById(request)
    }.getOrNull()

    suspend fun getStudentByUsername(username: String): StudentInfo? = runCatching {
        val request = GetStudentByUsernameRequest.newBuilder()
            .setUsername(username)
            .build()

        stub.getStudentByUsername(request)
    }.getOrNull()
}
