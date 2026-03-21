package com.b1nd.dodamdodam.user.domain.student.service

import com.b1nd.dodamdodam.user.domain.student.entity.StudentEntity
import com.b1nd.dodamdodam.user.domain.student.repository.StudentRepository
import org.springframework.stereotype.Service

@Service
class StudentService(
    private val repository: StudentRepository
) {
    fun create(student: StudentEntity) {
        repository.save(student)
    }
}