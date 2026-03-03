package com.b1nd.dodamdodam.user.application.user.data.response

import com.b1nd.dodamdodam.user.domain.student.entity.StudentEntity

data class StudentInfoResponse(
    val grade: Int,
    val room: Int,
    val number: Int
) {
    companion object {
        fun fromStudentEntity(entity: StudentEntity) = StudentInfoResponse(
            entity.grade,
            entity.room,
            entity.number
        )
    }
}