package com.b1nd.dodamdodam.auth.presentation.passport

import com.b1nd.dodamdodam.auth.application.passport.PassportUseCase
import com.b1nd.dodamdodam.auth.application.passport.data.response.ExchangePassportResponse
import com.b1nd.dodamdodam.core.common.data.Response
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/passport")
class PassportController(
    private val useCase: PassportUseCase
) {
    @PostMapping
    fun exchangePassport(): Response<ExchangePassportResponse> =
        useCase.exchange()
}