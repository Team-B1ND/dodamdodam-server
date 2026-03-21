package com.b1nd.dodamdodam.user.domain.user.repository

import com.b1nd.dodamdodam.user.domain.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository: JpaRepository<UserEntity, Long> {
    fun existsByUsername(username: String): Boolean
    fun findByPublicId(publicId: UUID): UserEntity?
    fun findAllByPublicIdIn(publicIds: Collection<UUID>): List<UserEntity>
    fun findByUsername(username: String): UserEntity?
    fun findByPhone(phone: String): UserEntity?
    fun existsByPhone(phone: String): Boolean
}
