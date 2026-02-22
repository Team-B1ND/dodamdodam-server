package com.b1nd.dodamdodam.user.presentation.user.http

import com.b1nd.dodamdodam.core.security.annotation.authentication.UserAccess
import com.b1nd.dodamdodam.user.application.user.UserUseCase
import com.b1nd.dodamdodam.user.application.user.data.request.StudentRegisterRequest
import com.b1nd.dodamdodam.user.application.user.data.request.TeacherRegisterRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userUseCase: UserUseCase
) {
    @UserAccess(enabledOnly = false)
    @PostMapping("/register-student")
    fun registerStudent(@RequestBody request: StudentRegisterRequest) =
        userUseCase.registerStudent(request)

    @UserAccess(enabledOnly = false)
    @PostMapping("/register-teacher")
    fun registerTeacher(@RequestBody request: TeacherRegisterRequest) =
        userUseCase.registerTeacher(request)
}