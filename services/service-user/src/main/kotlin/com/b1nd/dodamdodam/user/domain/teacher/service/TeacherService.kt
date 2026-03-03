package com.b1nd.dodamdodam.user.domain.teacher.service

import com.b1nd.dodamdodam.user.domain.teacher.entity.TeacherEntity
import com.b1nd.dodamdodam.user.domain.teacher.exception.TeacherNotFoundException
import com.b1nd.dodamdodam.user.domain.teacher.repository.TeacherRepository
import com.b1nd.dodamdodam.user.domain.user.entity.UserEntity
import org.springframework.stereotype.Service

@Service
class TeacherService(
    private val repository: TeacherRepository
) {
    fun create(teacher: TeacherEntity) {
        repository.save(teacher)
    }

    fun get(userEntity: UserEntity) =
        repository.findByUser(userEntity)
            ?: throw TeacherNotFoundException()

    fun getOrNull(userEntity: UserEntity): TeacherEntity? =
        repository.findByUser(userEntity)
}