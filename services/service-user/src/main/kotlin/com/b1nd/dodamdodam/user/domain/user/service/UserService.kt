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
import java.util.UUID

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

    fun get(publicId: UUID): UserEntity =
        userRepository.findByPublicId(publicId)
            ?: throw UserNotFoundException()

    fun getByPublicIds(publicIds: Collection<UUID>): List<UserEntity> =
        userRepository.findAllByPublicIdIn(publicIds)

    fun addRole(user: UserEntity, roles: List<RoleType>) {
        val userRoles = roles.map { UserRoleEntity(user, it) }
        userRoleRepository.saveAll(userRoles)
    }

    fun getRoles(user: UserEntity): Set<RoleType> =
        userRoleRepository.findAllByUser(user)
            .map { it.role }
            .toSet()

    fun getRolesMap(users: Collection<UserEntity>): Map<UserEntity, Set<RoleType>> =
        userRoleRepository.findAllByUserIn(users)
            .groupBy { it.user }
            .mapValues { (_, roles) -> roles.map { it.role }.toSet() }

    fun verify(username: String, password: String) {
        val user = userRepository.findByUsername(username)
            ?: throw UserPasswordIncorrectException()
        if (!encoder.matches(password, user.password))
            throw UserPasswordIncorrectException()
    }

    private fun checkDuplicateUser(username: String) {
        if (userRepository.existsByUsername(username))
            throw UserAlreadyExistsException()
    }
}