package com.b1nd.dodamdodam.user.presentation.user.grpc

import com.b1nd.dodamdodam.core.common.coroutine.CoroutineBlockingExecutor
import com.b1nd.dodamdodam.grpc.user.GetResidualStudentsRequest
import com.b1nd.dodamdodam.grpc.user.GetResidualStudentsResponse
import com.b1nd.dodamdodam.grpc.user.GetStudentsByUserIdsRequest
import com.b1nd.dodamdodam.grpc.user.GetStudentsByUserIdsResponse
import com.b1nd.dodamdodam.grpc.user.ResidualStudentDto
import com.b1nd.dodamdodam.grpc.user.StudentDto
import com.b1nd.dodamdodam.grpc.user.UserQueryServiceGrpcKt
import com.b1nd.dodamdodam.user.application.query.UserQueryUseCase
import net.devh.boot.grpc.server.service.GrpcService
import java.time.format.DateTimeFormatter
import java.util.UUID

@GrpcService
class UserQueryGrpcController(
    private val useCase: UserQueryUseCase,
    private val blockingExecutor: CoroutineBlockingExecutor
) : UserQueryServiceGrpcKt.UserQueryServiceCoroutineImplBase() {

    private val dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")

    override suspend fun getStudentsByUserIds(request: GetStudentsByUserIdsRequest): GetStudentsByUserIdsResponse {
        val students = blockingExecutor.execute {
            val userIds = request.userIdsList.map { UUID.fromString(it) }
            useCase.getStudentsByUserIds(userIds)
        }

        val dtos = students.map { student ->
            StudentDto.newBuilder()
                .setStudentId(student.id!!)
                .setUserId(student.user.publicId.toString())
                .setName(student.user.name)
                .setGrade(student.grade)
                .setRoom(student.room)
                .setNumber(student.number)
                .build()
        }

        return GetStudentsByUserIdsResponse.newBuilder()
            .addAllStudents(dtos)
            .build()
    }

    override suspend fun getResidualStudents(request: GetResidualStudentsRequest): GetResidualStudentsResponse {
        val pairs = blockingExecutor.execute {
            val absentUserIds = request.absentUserIdsList.map { UUID.fromString(it) }
            useCase.getResidualStudents(absentUserIds)
        }

        val dtos = pairs.map { (student, roleEntity) ->
            val user = student.user
            ResidualStudentDto.newBuilder()
                .setPublicId(user.publicId.toString())
                .setName(user.name)
                .setUsername(user.username)
                .setRole(roleEntity?.role?.name ?: "STUDENT")
                .setProfileImage(user.profileImage ?: "")
                .setPhone(user.phone ?: "")
                .setStudentId(student.id!!)
                .setGrade(student.grade)
                .setRoom(student.room)
                .setNumber(student.number)
                .setCreatedAt(student.user.createdAt?.format(dtFormatter) ?: "")
                .setModifiedAt(student.user.modifiedAt?.format(dtFormatter) ?: "")
                .build()
        }

        return GetResidualStudentsResponse.newBuilder()
            .addAllStudents(dtos)
            .build()
    }
}
