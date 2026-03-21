package com.b1nd.dodamdodam.user.domain.teacher.repository

import com.b1nd.dodamdodam.user.domain.teacher.entity.TeacherEntity
import com.b1nd.dodamdodam.user.domain.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TeacherRepository: JpaRepository<TeacherEntity, Long> {
    fun findByUser(user: UserEntity): TeacherEntity?
    fun findAllByUserIn(users: Collection<UserEntity>): List<TeacherEntity>
}