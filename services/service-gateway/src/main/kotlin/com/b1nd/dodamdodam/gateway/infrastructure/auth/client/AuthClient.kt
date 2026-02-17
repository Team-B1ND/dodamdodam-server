package com.b1nd.dodamdodam.gateway.infrastructure.auth.client

import com.b1nd.dodamdodam.gateway.infrastructure.auth.properties.AuthProperties
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class AuthClient(
    private val webClientBuilder: WebClient.Builder,
    private val properties: AuthProperties
) {
    private val webClient = webClientBuilder.baseUrl(properties.url).build()

    suspend fun exchangePassport(jwt: String?): String =
        webClient.post()
            .uri("/passport")
            .apply {
                jwt?.let { token ->
                    header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                }
            }
            .retrieve()
            .bodyToMono(String::class.java)
            .awaitSingle()
}