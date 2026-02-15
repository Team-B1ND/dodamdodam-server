package com.b1nd.dodamdodam.user.application.user

import com.b1nd.dodamdodam.user.application.user.data.request.StudentRegisterRequest
import com.b1nd.dodamdodam.user.application.user.data.request.VerifyPasswordRequest
import com.b1nd.dodamdodam.user.application.user.data.toStudentEntity
import com.b1nd.dodamdodam.user.application.user.data.toUserEntity
import com.b1nd.dodamdodam.user.domain.student.service.StudentService
import com.b1nd.dodamdodam.user.domain.user.entity.UserEntity
import com.b1nd.dodamdodam.user.domain.user.enumeration.RoleType
import com.b1nd.dodamdodam.user.domain.user.service.UserService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Component

@Component
@Transactional(rollbackOn = [Exception::class])
class UserUseCase(
    private val userService: UserService,
    private val studentService: StudentService
) {
    fun registerStudent(request: StudentRegisterRequest) {
        //TODO 전화번호 검증 로직
        val savedUser: UserEntity = userService.create(
            request.toUserEntity(),
            RoleType.STUDENT
        )
        studentService.create(request.toStudentEntity(savedUser))
    }

    fun verifyPassword(request: VerifyPasswordRequest) {
        userService.verify(request.username, request.password)
    }
}