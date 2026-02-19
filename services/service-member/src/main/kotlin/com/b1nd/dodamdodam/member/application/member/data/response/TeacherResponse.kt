package com.b1nd.dodamdodam.member.application.member.data.response

import com.b1nd.dodamdodam.member.domain.teacher.entity.TeacherEntity

data class TeacherResponse(
    val id: Long,
    val name: String,
    val tel: String,
    val position: String,
) {
    companion object {
        fun fromEntity(teacher: TeacherEntity) = TeacherResponse(
            id = teacher.id!!,
            name = teacher.member.name,
            tel = teacher.tel,
            position = teacher.position,
        )
    }
}
