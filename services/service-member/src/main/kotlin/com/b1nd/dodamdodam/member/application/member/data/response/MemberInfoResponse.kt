package com.b1nd.dodamdodam.member.application.member.data.response

import com.b1nd.dodamdodam.member.domain.member.entity.MemberEntity
import com.b1nd.dodamdodam.member.domain.member.entity.MemberRoleEntity
import com.b1nd.dodamdodam.member.domain.student.entity.StudentEntity
import com.b1nd.dodamdodam.member.domain.teacher.entity.TeacherEntity
import java.time.LocalDateTime

data class MemberInfoResponse(
    val id: Long,
    val name: String,
    val email: String?,
    val roles: List<String>,
    val profileImage: String?,
    val phone: String,
    val student: StudentResponse?,
    val teacher: TeacherResponse?,
    val createdAt: LocalDateTime?,
    val modifiedAt: LocalDateTime?,
) {
    companion object {
        fun fromEntity(
            member: MemberEntity,
            roles: List<MemberRoleEntity>,
            student: StudentEntity?,
            teacher: TeacherEntity?,
        ): MemberInfoResponse {
            val roleNames = mutableListOf(member.status.name)
            roles.forEach { roleNames.add(it.role.name) }
            return MemberInfoResponse(
                id = member.id!!,
                name = member.name,
                email = member.email,
                roles = roleNames,
                profileImage = member.profileImage,
                phone = member.phone,
                student = student?.let { StudentResponse.fromEntity(it) },
                teacher = teacher?.let { TeacherResponse.fromEntity(it) },
                createdAt = member.createdAt,
                modifiedAt = member.modifiedAt,
            )
        }
    }
}
