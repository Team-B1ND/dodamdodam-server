package com.b1nd.dodamdodam.user.domain.student.repository

import com.b1nd.dodamdodam.user.domain.student.entity.StudentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface StudentRepository: JpaRepository<StudentEntity, Long> {
    fun findByUserPublicIdIn(userIds: List<UUID>): List<StudentEntity>
    fun findByUserPublicIdNotIn(userIds: List<UUID>): List<StudentEntity>
    fun findAllByOrderByUserIdAsc(): List<StudentEntity>

    @Query("SELECT s FROM StudentEntity s JOIN FETCH s.user WHERE s.user.publicId IN :userIds")
    fun findByUserPublicIdInFetchUser(userIds: List<UUID>): List<StudentEntity>
}