package com.b1nd.dodamdodam.auth.presentation.auth

import com.b1nd.dodamdodam.auth.application.auth.AuthUseCase
import com.b1nd.dodamdodam.auth.application.auth.data.request.LoginRequest
import com.b1nd.dodamdodam.auth.application.auth.data.response.LoginResponse
import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.core.security.annotation.authentication.UserAccess
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val useCase: AuthUseCase
) {
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): Response<LoginResponse> =
        useCase.login(request)

    @GetMapping("/health")
    fun health(): String = "OK"

    @UserAccess(enabledOnly = true)
    @GetMapping("/test")
    fun test(): String = "TEST"
}