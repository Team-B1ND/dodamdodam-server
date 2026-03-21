package com.b1nd.dodamdodam.user.domain.student.service

import com.b1nd.dodamdodam.user.domain.student.entity.StudentEntity
import com.b1nd.dodamdodam.user.domain.student.repository.StudentRepository
import com.b1nd.dodamdodam.user.domain.user.entity.UserEntity
import org.springframework.stereotype.Service

@Service
class StudentService(
    private val repository: StudentRepository
) {
    fun create(student: StudentEntity) {
        repository.save(student)
    }

    fun getByUser(user: UserEntity): StudentEntity? =
        repository.findByUser(user)

    fun getByUsers(users: Collection<UserEntity>): Map<UserEntity, StudentEntity> =
        repository.findAllByUserIn(users).associateBy { it.user }

    fun getAll(): List<StudentEntity> =
        repository.findAll()
}