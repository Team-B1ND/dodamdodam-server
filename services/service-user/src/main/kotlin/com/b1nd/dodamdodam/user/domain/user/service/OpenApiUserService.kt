package com.b1nd.dodamdodam.user.domain.user.service

import com.b1nd.dodamdodam.user.domain.student.repository.StudentRepository
import com.b1nd.dodamdodam.user.domain.teacher.repository.TeacherRepository
import com.b1nd.dodamdodam.user.domain.user.data.StudentDetails
import com.b1nd.dodamdodam.user.domain.user.data.TeacherDetails
import com.b1nd.dodamdodam.user.domain.user.data.UserWithDetails
import com.b1nd.dodamdodam.user.domain.user.repository.UserRepository
import com.b1nd.dodamdodam.user.domain.user.repository.UserRoleRepository
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class OpenApiUserService(
    private val userRepository: UserRepository,
    private val userRoleRepository: UserRoleRepository,
    private val studentRepository: StudentRepository,
    private val teacherRepository: TeacherRepository,
    private val cacheManager: CacheManager,
) {
    companion object {
        private const val CACHE_NAME = "openapi:user"
    }

    fun getUsersWithDetails(publicIds: Collection<UUID>): List<UserWithDetails> {
        val cache = cacheManager.getCache(CACHE_NAME)
        val cachedResults = mutableListOf<UserWithDetails>()
        val missedIds = mutableListOf<UUID>()

        publicIds.forEach { publicId ->
            val cached = cache?.get(publicId, UserWithDetails::class.java)
            if (cached != null) {
                cachedResults.add(cached)
            } else {
                missedIds.add(publicId)
            }
        }

        if (missedIds.isNotEmpty()) {
            val fetched = fetchFromDb(missedIds)
            fetched.forEach { user ->
                cache?.put(user.publicId, user)
                cachedResults.add(user)
            }
        }

        return cachedResults
    }

    private fun fetchFromDb(publicIds: Collection<UUID>): List<UserWithDetails> {
        val users = userRepository.findAllByPublicIdIn(publicIds)
        if (users.isEmpty()) return emptyList()

        val roles = userRoleRepository.findAllByUserIn(users)
            .groupBy { it.user.id }
            .mapValues { (_, v) -> v.map { it.role }.toSet() }

        val students = studentRepository.findAllByUserIn(users)
            .associateBy { it.user.id }

        val teachers = teacherRepository.findAllByUserIn(users)
            .associateBy { it.user.id }

        return users.map { user ->
            val userId = user.id
            val student = students[userId]
            val teacher = teachers[userId]

            UserWithDetails(
                publicId = user.publicId!!,
                username = user.username,
                name = user.name,
                phone = user.phone,
                profileImage = user.profileImage,
                status = user.status,
                roles = roles[userId] ?: emptySet(),
                student = student?.let { StudentDetails(it.grade, it.room, it.number) },
                teacher = teacher?.let { TeacherDetails(it.position) },
                createdAt = user.createdAt!!,
            )
        }
    }
}