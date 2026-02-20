package com.b1nd.dodamdodam.user.application.user.data

import com.b1nd.dodamdodam.user.application.user.data.request.StudentRegisterRequest
import com.b1nd.dodamdodam.user.application.user.data.request.TeacherRegisterRequest
import com.b1nd.dodamdodam.user.domain.student.entity.StudentEntity
import com.b1nd.dodamdodam.user.domain.teacher.entity.TeacherEntity
import com.b1nd.dodamdodam.user.domain.user.entity.UserEntity
import com.b1nd.dodamdodam.user.domain.user.enumeration.StatusType

fun StudentRegisterRequest.toUserEntity(): UserEntity {
    return UserEntity(
        username = username,
        password = password,
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

fun TeacherRegisterRequest.toUserEntity(): UserEntity {
    return UserEntity(
        username = username,
        password = password,
        name = name,
        phone = phone,
        status = StatusType.PENDING,
    )
}

fun TeacherRegisterRequest.toTeacherEntity(user: UserEntity): TeacherEntity {
    return TeacherEntity(
        position = position,
        user = user
    )
}