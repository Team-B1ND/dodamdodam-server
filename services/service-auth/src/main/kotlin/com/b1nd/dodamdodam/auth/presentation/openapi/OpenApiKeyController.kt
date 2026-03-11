package com.b1nd.dodamdodam.auth.presentation.openapi

import com.b1nd.dodamdodam.auth.application.openapi.OpenApiKeyUseCase
import com.b1nd.dodamdodam.core.common.data.Response
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/open-api/verify")
class OpenApiKeyController(
    private val openApiKeyUseCase: OpenApiKeyUseCase
) {
    @GetMapping
    fun verifyApiKey(
        @RequestParam appId: String,
        @RequestParam apiKey: String
    ): Response<Map<String, Boolean>> {
        val isValid = openApiKeyUseCase.verify(appId, apiKey)
        return Response.ok("API Key 검증 완료.", mapOf("valid" to isValid))
    }
}