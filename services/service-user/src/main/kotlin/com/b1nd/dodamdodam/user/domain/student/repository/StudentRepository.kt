package com.b1nd.dodamdodam.user.domain.student.repository

import com.b1nd.dodamdodam.user.domain.student.entity.StudentEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface StudentRepository: JpaRepository<StudentEntity, Long> {
    fun findByUserPublicIdIn(userIds: List<UUID>): List<StudentEntity>
    fun findByUserPublicIdNotIn(userIds: List<UUID>): List<StudentEntity>
    fun findAllByOrderByUserIdAsc(): List<StudentEntity>
}