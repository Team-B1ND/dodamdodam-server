package com.b1nd.dodamdodam.user.domain.student.repository

import com.b1nd.dodamdodam.user.domain.student.entity.StudentEntity
import com.b1nd.dodamdodam.user.domain.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface StudentRepository: JpaRepository<StudentEntity, Long> {
    fun findByUser(user: UserEntity): StudentEntity?
    fun findAllByUserIn(users: Collection<UserEntity>): List<StudentEntity>
}