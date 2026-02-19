package com.b1nd.dodamdodam.user.application.query

import com.b1nd.dodamdodam.user.domain.student.entity.StudentEntity
import com.b1nd.dodamdodam.user.domain.student.repository.StudentRepository
import com.b1nd.dodamdodam.user.domain.user.entity.UserRoleEntity
import com.b1nd.dodamdodam.user.domain.user.repository.UserRoleRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Component
@Transactional(readOnly = true)
class UserQueryUseCase(
    private val studentRepository: StudentRepository,
    private val userRoleRepository: UserRoleRepository
) {
    fun getStudentsByUserIds(userIds: List<UUID>): List<StudentEntity> =
        studentRepository.findByUserPublicIdIn(userIds)

    fun getResidualStudents(absentUserIds: List<UUID>): List<Pair<StudentEntity, UserRoleEntity?>> {
        val residualStudents = if (absentUserIds.isEmpty()) {
            studentRepository.findAllByOrderByUserIdAsc()
        } else {
            studentRepository.findByUserPublicIdNotIn(absentUserIds)
        }
        return residualStudents.map { student ->
            student to userRoleRepository.findFirstByUser(student.user)
        }
    }
}
