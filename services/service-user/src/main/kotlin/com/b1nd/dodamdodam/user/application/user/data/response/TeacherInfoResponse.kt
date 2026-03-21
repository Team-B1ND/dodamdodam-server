package com.b1nd.dodamdodam.user.application.user.data.response

import com.b1nd.dodamdodam.user.domain.teacher.entity.TeacherEntity

data class TeacherInfoResponse(
    val position: String,
) {
    companion object {
        fun fromTeacherEntity(entity: TeacherEntity) =
            TeacherInfoResponse(entity.position)
    }
}