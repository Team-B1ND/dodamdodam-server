package com.b1nd.dodamdodam.user.application.openapi.data

import com.b1nd.dodamdodam.grpc.user.GetUsersResponse
import com.b1nd.dodamdodam.grpc.user.StudentInfo
import com.b1nd.dodamdodam.grpc.user.TeacherInfo
import com.b1nd.dodamdodam.grpc.user.UserResponse
import com.b1nd.dodamdodam.user.application.user.data.response.StudentInfoResponse
import com.b1nd.dodamdodam.user.application.user.data.response.TeacherInfoResponse
import com.b1nd.dodamdodam.user.application.user.data.response.UserInfoResponse
import com.b1nd.dodamdodam.user.domain.user.data.UserWithDetails

fun UserWithDetails.toUserInfoResponse() = UserInfoResponse(
    publicId = publicId,
    username = username,
    name = name,
    phone = phone,
    profileImage = profileImage,
    status = status,
    roles = roles,
    student = student?.let { StudentInfoResponse(it.grade, it.room, it.number) },
    teacher = teacher?.let { TeacherInfoResponse(it.position) },
    createdAt = createdAt,
)

fun UserWithDetails.toGrpcResponse(): UserResponse {
    val builder = UserResponse.newBuilder()
        .setPublicId(publicId.toString())
        .setUsername(username)
        .setName(name)
        .setStatus(status.name)
        .addAllRoles(roles.map { it.name })
        .setCreatedAt(createdAt.toString())

    phone?.let { builder.setPhone(it) }
    profileImage?.let { builder.setProfileImage(it) }
    student?.let {
        builder.setStudent(
            StudentInfo.newBuilder()
                .setGrade(it.grade)
                .setRoom(it.room)
                .setNumber(it.number)
                .build()
        )
    }
    teacher?.let {
        builder.setTeacher(
            TeacherInfo.newBuilder()
                .setPosition(it.position)
                .build()
        )
    }

    return builder.build()
}

fun List<UserWithDetails>.toGetUsersGrpcResponse(): GetUsersResponse =
    GetUsersResponse.newBuilder()
        .addAllUsers(map { it.toGrpcResponse() })
        .build()
