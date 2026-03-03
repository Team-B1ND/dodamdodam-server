package com.b1nd.dodamdodam.user.application.user.data.response

import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
import com.b1nd.dodamdodam.user.domain.student.entity.StudentEntity
import com.b1nd.dodamdodam.user.domain.teacher.entity.TeacherEntity
import com.b1nd.dodamdodam.user.domain.user.entity.UserEntity
import com.b1nd.dodamdodam.user.domain.user.enumeration.StatusType
import java.time.LocalDateTime
import java.util.UUID

data class UserInfoResponse(
    val publicId: UUID,
    val username: String,
    val name: String,
    val phone: String?,
    val profileImage: String?,
    val status: StatusType,
    val roles: Set<RoleType>,
    val student: StudentInfoResponse? = null,
    val teacher: TeacherInfoResponse? = null,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun fromEntity(user: UserEntity, roles: Set<RoleType>, student: StudentEntity?, teacher: TeacherEntity?) =
            UserInfoResponse(
                user.publicId!!,
                user.username,
                user.name,
                user.phone,
                user.profileImage,
                user.status,
                roles,
                student?.let { StudentInfoResponse.fromStudentEntity(student) },
                teacher?.let { TeacherInfoResponse.fromTeacherEntity(teacher) },
                user.createdAt!!
            )
    }
}