package com.b1nd.dodamdodam.member.application.member.data.response

import com.b1nd.dodamdodam.member.domain.student.entity.StudentEntity

data class StudentResponse(
    val id: Long,
    val name: String,
    val grade: Int,
    val room: Int,
    val number: Int,
) {
    companion object {
        fun fromEntity(student: StudentEntity) = StudentResponse(
            id = student.id!!,
            name = student.member.name,
            grade = student.grade,
            room = student.room,
            number = student.number,
        )
    }
}
