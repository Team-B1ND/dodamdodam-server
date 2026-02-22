package com.b1nd.dodamdodam.user.domain.teacher.service

import com.b1nd.dodamdodam.user.domain.teacher.entity.TeacherEntity
import com.b1nd.dodamdodam.user.domain.teacher.repository.TeacherRepository
import org.springframework.stereotype.Service

@Service
class TeacherService(
    private val repository: TeacherRepository
) {
    fun create(teacher: TeacherEntity) {
        repository.save(teacher)
    }
}