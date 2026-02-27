package com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.response

import com.b1nd.dodamdodam.grpc.user.ResidualStudent
import java.util.UUID

data class ResidualStudentResponse(
    val id: UUID,
    val name: String,
    val email: String,
    val role: String,
    val profileImage: String?,
    val phone: String?,
    val student: ResidualStudentInfo,
    val createdAt: String,
    val modifiedAt: String
) {
    companion object {
        fun from(dto: ResidualStudent): ResidualStudentResponse =
            ResidualStudentResponse(
                id = UUID.fromString(dto.publicId),
                name = dto.name,
                email = dto.username,
                role = dto.role,
                profileImage = dto.profileImage.takeIf { it.isNotBlank() },
                phone = dto.phone.takeIf { it.isNotBlank() },
                student = ResidualStudentInfo(
                    id = dto.studentId,
                    name = dto.name,
                    grade = dto.grade,
                    room = dto.room,
                    number = dto.number
                ),
                createdAt = dto.createdAt,
                modifiedAt = dto.modifiedAt
            )
    }
}

data class ResidualStudentInfo(
    val id: Long,
    val name: String,
    val grade: Int,
    val room: Int,
    val number: Int
)
