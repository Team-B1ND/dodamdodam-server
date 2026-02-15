package com.b1nd.dodamdodam.user.application.user.data

import com.b1nd.dodamdodam.user.application.user.data.request.StudentRegisterRequest
import com.b1nd.dodamdodam.user.domain.student.entity.StudentEntity
import com.b1nd.dodamdodam.user.domain.user.entity.UserEntity
import com.b1nd.dodamdodam.user.domain.user.enumeration.StatusType

fun StudentRegisterRequest.toUserEntity(encodedPassword: String): UserEntity {
    return UserEntity(
        username = username,
        password = encodedPassword,
        name = name,
        phone = phone,
        status = StatusType.PENDING,
    )
}

fun StudentRegisterRequest.toStudentEntity(user: UserEntity): StudentEntity {
    return StudentEntity(
        grade = grade,
        room = room,
        number = number,
        user = user
    )
}