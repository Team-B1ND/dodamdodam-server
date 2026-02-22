package com.b1nd.dodamdodam.user.application.user.data

import com.b1nd.dodamdodam.core.kafka.event.user.UserCreatedEvent
import com.b1nd.dodamdodam.core.kafka.event.user.UserUpdatedEvent
import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
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

fun UserEntity.toUserCreatedEvent(role: RoleType): UserCreatedEvent =
    UserCreatedEvent(
        publicId = publicId!!,
        username = username,
        status = status == StatusType.ACTIVE,
        name = name,
        phone = phone,
        profileImage = profileImage,
        role = role.name
    )

fun UserEntity.toUserUpdatedEvent(roles: Collection<RoleType>): UserUpdatedEvent =
    UserUpdatedEvent(
        publicId = publicId!!,
        username = username,
        status = status == StatusType.ACTIVE,
        roles = roles.map { it.name },
        name = name,
        phone = phone,
        profileImage = profileImage
    )
