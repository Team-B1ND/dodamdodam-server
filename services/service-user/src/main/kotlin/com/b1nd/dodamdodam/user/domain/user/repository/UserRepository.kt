package com.b1nd.dodamdodam.user.domain.user.repository

import com.b1nd.dodamdodam.user.domain.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository: JpaRepository<UserEntity, Long> {
    fun existsByUsername(username: String): Boolean
    fun findByUsername(username: String): UserEntity?
    fun findByPublicId(publicId: UUID): UserEntity?
    fun findByPublicIdIn(publicIds: List<UUID>): List<UserEntity>
}