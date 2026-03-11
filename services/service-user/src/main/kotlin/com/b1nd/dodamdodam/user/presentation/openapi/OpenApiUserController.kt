package com.b1nd.dodamdodam.user.presentation.openapi

import com.b1nd.dodamdodam.user.application.openapi.OpenApiUserUseCase
import com.b1nd.dodamdodam.user.application.openapi.data.request.GetUsersByPublicIdsRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/openapi")
class OpenApiUserController(
    private val openApiUserUseCase: OpenApiUserUseCase,
) {
    @PostMapping
    fun getUsers(@RequestBody request: GetUsersByPublicIdsRequest) =
        openApiUserUseCase.getUsersResponse(request)
}