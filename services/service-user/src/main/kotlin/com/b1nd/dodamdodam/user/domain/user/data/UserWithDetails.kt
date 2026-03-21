package com.b1nd.dodamdodam.user.domain.user.data

import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
import com.b1nd.dodamdodam.user.domain.user.enumeration.StatusType
import java.time.LocalDateTime
import java.util.UUID

data class UserWithDetails(
    val publicId: UUID,
    val username: String,
    val name: String,
    val phone: String?,
    val profileImage: String?,
    val status: StatusType,
    val roles: Set<RoleType>,
    val student: StudentDetails?,
    val teacher: TeacherDetails?,
    val createdAt: LocalDateTime,
)

data class StudentDetails(
    val grade: Int,
    val room: Int,
    val number: Int,
)

data class TeacherDetails(
    val position: String,
)