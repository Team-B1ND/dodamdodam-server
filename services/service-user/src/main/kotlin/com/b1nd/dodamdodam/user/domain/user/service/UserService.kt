package com.b1nd.dodamdodam.user.domain.user.service

import com.b1nd.dodamdodam.user.domain.user.entity.UserEntity
import com.b1nd.dodamdodam.user.domain.user.entity.UserRoleEntity
import com.b1nd.dodamdodam.user.domain.user.enumeration.RoleType
import com.b1nd.dodamdodam.user.domain.user.exception.UserAlreadyExistsException
import com.b1nd.dodamdodam.user.domain.user.repository.UserRepository
import com.b1nd.dodamdodam.user.domain.user.repository.UserRoleRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userRoleRepository: UserRoleRepository
) {
    fun create(user: UserEntity, role: RoleType): UserEntity {
        checkDuplicateUser(user.username)
        val savedUser = userRepository.save(user)
        addRole(savedUser, listOf(role))
        return savedUser
    }

    fun addRole(user: UserEntity, roles: List<RoleType>) {
        val userRoles = roles.map { UserRoleEntity(user, it) }
        userRoleRepository.saveAll(userRoles)
    }

    private fun checkDuplicateUser(username: String) {
        if (userRepository.existsByUsername(username))
            throw UserAlreadyExistsException()
    }
}