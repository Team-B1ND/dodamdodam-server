package com.b1nd.dodamdodam.auth.controller

import com.b1nd.dodamdodam.core.common.response.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController {

    @GetMapping("/health")
    fun health(): ApiResponse<String> {
        return ApiResponse.success("Auth Service is running")
    }
}
