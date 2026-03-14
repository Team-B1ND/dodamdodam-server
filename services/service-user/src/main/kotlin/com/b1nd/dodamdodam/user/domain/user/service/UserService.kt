package com.b1nd.dodamdodam.user.domain.user.service

import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
import com.b1nd.dodamdodam.user.application.user.data.request.UpdateUserInfoRequest
import com.b1nd.dodamdodam.user.domain.user.entity.UserEntity
import com.b1nd.dodamdodam.user.domain.user.entity.UserRoleEntity
import com.b1nd.dodamdodam.user.domain.user.enumeration.StatusType
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
    fun get(publicId: UUID): UserEntity =
        userRepository.findByPublicId(publicId)
            ?: throw UserNotFoundException()

    fun getByPublicIds(publicIds: Collection<UUID>): List<UserEntity> =
        userRepository.findAllByPublicIdIn(publicIds)

    fun create(user: UserEntity, role: RoleType): UserEntity {
        checkDuplicateUser(user.username)
        user.updatePassword(encoder.encode(user.password))
        val savedUser = userRepository.save(user)
        addRole(savedUser, setOf(role))
        return savedUser
    }

    fun update(publicId: UUID, name: String?, phone: String?, profileImage: String?): UserEntity {
        val user = userRepository.findByPublicId(publicId)
            ?: throw UserNotFoundException()
        user.updateInfo(name, phone, profileImage)
        return userRepository.save(user)
    }

    fun delete(publicId: UUID): UserEntity {
        val user = userRepository.findByPublicId(publicId)
            ?: throw UserNotFoundException()
        user.status = StatusType.DEACTIVATED
        return userRepository.save(user)
    }

    fun enable(publicId: UUID): UserEntity {
        val user = userRepository.findByPublicId(publicId)
            ?: throw UserNotFoundException()
        user.status = StatusType.ACTIVE
        return userRepository.save(user)
    }

    fun updatePassword(publicId: UUID, postPassword: String, newPassword: String) {
        val user = userRepository.findByPublicId(publicId)
            ?: throw UserNotFoundException()
        if (!encoder.matches(postPassword, user.password)) throw UserPasswordIncorrectException()

        user.updatePassword(encoder.encode(newPassword))
        userRepository.save(user)
    }

    fun addRole(user: UserEntity, roles: Set<RoleType>) {
        val existingRoles = getRoles(user)
        val rolesToAdd = roles.subtract(existingRoles)
        if (rolesToAdd.isEmpty()) return

        val userRoles = rolesToAdd.map { UserRoleEntity(user, it) }
        userRoleRepository.saveAll(userRoles)
    }

    fun getAll(): List<UserEntity> =
        userRepository.findAll()

    fun getRoles(user: UserEntity): Set<RoleType> =
        userRoleRepository.findAllByUser(user)
            .map { it.role }
            .toSet()

    fun getRolesGroupedByUser(users: Collection<UserEntity>): Map<Long?, Set<RoleType>> =
        userRoleRepository.findAllByUserIn(users)
            .groupBy { it.user.id }
            .mapValues { (_, v) -> v.map { it.role }.toSet() }

    fun verify(username: String, password: String) {
        val user = userRepository.findByUsername(username)
            ?: throw UserPasswordIncorrectException()
        if (!encoder.matches(password, user.password))
            throw UserPasswordIncorrectException()
    }

    fun getByUsername(username: String): UserEntity =
        userRepository.findByUsername(username) ?: throw UserNotFoundException()

    private fun checkDuplicateUser(username: String) {
        if (userRepository.existsByUsername(username))
            throw UserAlreadyExistsException()
    }
}
