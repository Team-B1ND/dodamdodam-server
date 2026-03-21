package com.b1nd.dodamdodam.user.domain.student.service

import com.b1nd.dodamdodam.user.domain.student.entity.StudentEntity
import com.b1nd.dodamdodam.user.domain.student.exception.StudentNotFoundException
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

    fun get(user: UserEntity) =
        repository.findByUser(user)
            ?: throw StudentNotFoundException()

    fun getOrNull(user: UserEntity): StudentEntity? =
        repository.findByUser(user)

    fun getByUsers(users: Collection<UserEntity>): Map<Long?, StudentEntity> =
        repository.findAllByUserIn(users).associateBy { it.user.id }

    fun getAll(): List<StudentEntity> =
        repository.findAll()

    fun update(user: UserEntity, grade: Int?, room: Int?, number: Int?) {
        val student = repository.findByUser(user) ?: throw StudentNotFoundException()
        student.updateInfo(grade, room, number)
    }
}