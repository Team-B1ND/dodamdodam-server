package com.b1nd.dodamdodam.user.presentation.user.http

import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.core.security.annotation.authentication.UserAccess
import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
import com.b1nd.dodamdodam.user.application.user.UserUseCase
import com.b1nd.dodamdodam.user.application.user.data.request.ChangePasswordRequest
import com.b1nd.dodamdodam.user.application.user.data.request.EnableUserRequest
import com.b1nd.dodamdodam.user.application.user.data.request.StudentRegisterRequest
import com.b1nd.dodamdodam.user.application.user.data.request.TeacherRegisterRequest
import com.b1nd.dodamdodam.user.application.user.data.request.UpdateUserInfoRequest
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
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

    @UserAccess
    @PatchMapping
    fun editUserInfo(@RequestBody request: UpdateUserInfoRequest) =
        userUseCase.updateUser(request)

    @UserAccess
    @PatchMapping("/change-password")
    fun changePassword(@RequestBody request: ChangePasswordRequest) =
        userUseCase.changePassword(request)

    @UserAccess(roles = [RoleType.ADMIN])
    @PostMapping("/enable-user")
    fun enableUser(@RequestBody request: EnableUserRequest) =
        userUseCase.enableUser(request)

    @UserAccess
    @DeleteMapping
    fun quitUser(): Response<Any> =
        userUseCase.quitUser()
}