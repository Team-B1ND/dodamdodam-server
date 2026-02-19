package com.b1nd.dodamdodam.gateway.infrastructure.auth.client

import com.b1nd.dodamdodam.core.common.exception.BasicException
import com.b1nd.dodamdodam.core.common.exception.base.BaseInternalServerException
import com.b1nd.dodamdodam.gateway.infrastructure.auth.client.data.ExchangePassportResponse
import com.b1nd.dodamdodam.gateway.infrastructure.auth.exception.PassportExchangeFailedException
import com.b1nd.dodamdodam.gateway.infrastructure.auth.properties.AuthProperties
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

@Component
class AuthClient(
    private val webClientBuilder: WebClient.Builder,
    private val properties: AuthProperties
) {
    private val webClient = webClientBuilder.baseUrl(properties.url).build()

    suspend fun exchangePassport(jwt: String?): String {
        return webClient.post()
            .uri("/passport")
            .apply {
                jwt?.let { token ->
                    header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                }
            }
            .retrieve()
            .onStatus({ it.is4xxClientError }) { Mono.error(PassportExchangeFailedException()) }
            .onStatus({ it.is5xxServerError }) { Mono.error(BaseInternalServerException())}
            .bodyToMono<ExchangePassportResponse>()
            .map { it.passport }
            .awaitSingle()
    }
}