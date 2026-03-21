package com.b1nd.dodamdodam.user.domain.user.repository

import com.b1nd.dodamdodam.user.domain.user.entity.UserEntity
import com.b1nd.dodamdodam.user.domain.user.entity.UserRoleEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRoleRepository: JpaRepository<UserRoleEntity, Long> {
    fun findAllByUser(user: UserEntity): List<UserRoleEntity>
    fun findAllByUserIn(users: Collection<UserEntity>): List<UserRoleEntity>
}
