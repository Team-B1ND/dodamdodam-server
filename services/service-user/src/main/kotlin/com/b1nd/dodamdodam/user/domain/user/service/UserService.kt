package com.b1nd.dodamdodam.user.domain.user.service

import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
import com.b1nd.dodamdodam.user.domain.user.entity.UserEntity
import com.b1nd.dodamdodam.user.domain.user.entity.UserRoleEntity
import com.b1nd.dodamdodam.user.domain.user.exception.UserAlreadyExistsException
import com.b1nd.dodamdodam.user.domain.user.exception.UserNotFoundException
import com.b1nd.dodamdodam.user.domain.user.exception.UserPasswordIncorrectException
import com.b1nd.dodamdodam.user.domain.user.repository.UserRepository
import com.b1nd.dodamdodam.user.domain.user.repository.UserRoleRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userRoleRepository: UserRoleRepository,
    private val encoder: BCryptPasswordEncoder
) {
    fun create(user: UserEntity, role: RoleType): UserEntity {
        checkDuplicateUser(user.username)
        user.updatePassword(encoder.encode(user.password))
        val savedUser = userRepository.save(user)
        addRole(savedUser, listOf(role))
        return savedUser
    }

    fun addRole(user: UserEntity, roles: List<RoleType>) {
        val userRoles = roles.map { UserRoleEntity(user, it) }
        userRoleRepository.saveAll(userRoles)
    }

    fun verify(username: String, password: String) {
        val user = userRepository.findByUsername(username)
            ?: throw UserPasswordIncorrectException()
        if (!encoder.matches(password, user.password))
            throw UserPasswordIncorrectException()
    }

    fun getByUsername(username: String): UserEntity {
        return userRepository.findByUsername(username)
            ?: throw UserNotFoundException()
    }

    fun getByUsernames(usernames: List<String>): List<UserEntity> {
        return userRepository.findByUsernameIn(usernames)
    }

    private fun checkDuplicateUser(username: String) {
        if (userRepository.existsByUsername(username))
            throw UserAlreadyExistsException()
    }
}