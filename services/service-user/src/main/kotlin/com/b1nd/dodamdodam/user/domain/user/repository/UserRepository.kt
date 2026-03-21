package com.b1nd.dodamdodam.user.domain.user.repository

import com.b1nd.dodamdodam.user.domain.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<UserEntity, Long> {
    fun existsByUsername(username: String): Boolean
    fun findByUsername(username: String): UserEntity?
}