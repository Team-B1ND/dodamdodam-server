package com.b1nd.dodamdodam.user.application.user

import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
import com.b1nd.dodamdodam.user.application.user.data.request.StudentRegisterRequest
import com.b1nd.dodamdodam.user.application.user.data.request.TeacherRegisterRequest
import com.b1nd.dodamdodam.user.application.user.data.request.VerifyPasswordRequest
import com.b1nd.dodamdodam.user.application.user.data.toStudentEntity
import com.b1nd.dodamdodam.user.application.user.data.toTeacherEntity
import com.b1nd.dodamdodam.user.application.user.data.toUserEntity
import com.b1nd.dodamdodam.user.domain.student.service.StudentService
import com.b1nd.dodamdodam.user.domain.teacher.service.TeacherService
import com.b1nd.dodamdodam.user.domain.user.entity.UserEntity
import com.b1nd.dodamdodam.user.domain.user.service.UserService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Component

@Component
@Transactional(rollbackOn = [Exception::class])
class UserUseCase(
    private val userService: UserService,
    private val studentService: StudentService,
    private val teacherService: TeacherService
) {
    fun registerStudent(request: StudentRegisterRequest) {
        //TODO 전화번호 검증 로직
        val savedUser: UserEntity = userService.create(
            request.toUserEntity(),
            RoleType.STUDENT
        )
        studentService.create(request.toStudentEntity(savedUser))
    }

    fun registerTeacher(request: TeacherRegisterRequest) {
        //TODO 전화번호 검증 로직
        val savedUser: UserEntity = userService.create(
            request.toUserEntity(),
            RoleType.TEACHER,
        )
        teacherService.create(request.toTeacherEntity(savedUser))
    }

    fun verifyPassword(request: VerifyPasswordRequest): Boolean {
        try {
            userService.verify(request.username, request.password)
            return true
        } catch (_: Exception) {
            return false
        }
    }
}