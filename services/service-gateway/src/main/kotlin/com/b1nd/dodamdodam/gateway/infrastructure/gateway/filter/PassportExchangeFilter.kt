package com.b1nd.dodamdodam.gateway.infrastructure.gateway.filter

import com.b1nd.dodamdodam.core.common.data.webclient.WebClientApiResponse
import com.b1nd.dodamdodam.core.common.exception.base.BaseInternalServerException
import com.b1nd.dodamdodam.core.common.exception.auth.AuthTokenExceptionCode
import com.b1nd.dodamdodam.core.common.exception.auth.InvalidTokenSignatureException
import com.b1nd.dodamdodam.core.common.exception.auth.TokenExpiredException
import com.b1nd.dodamdodam.gateway.domain.passport.repository.PassportCacheRepository
import com.b1nd.dodamdodam.gateway.infrastructure.auth.client.data.ExchangePassportResponse
import com.b1nd.dodamdodam.gateway.infrastructure.auth.properties.AuthProperties
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.core.Ordered
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequestDecorator
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.time.Duration

@Component
class PassportExchangeFilter(
    private val webClientBuilder: WebClient.Builder,
    private val properties: AuthProperties,
    private val repository: PassportCacheRepository
): GlobalFilter, Ordered {
    private val webClient = webClientBuilder.baseUrl(properties.url).build()
    
    override fun getOrder(): Int = 0

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val jwt = exchange.request.headers
            .getFirst(HttpHeaders.AUTHORIZATION)
            ?.removePrefix("Bearer ")
            ?.trim()

        return extractPassport(jwt)
            .flatMap { passport ->
                val decoratedRequest = object : ServerHttpRequestDecorator(exchange.request) {
                    override fun getHeaders(): HttpHeaders {
                        val headers = HttpHeaders()
                        headers.putAll(super.getHeaders())
                        headers.set("X-User-Passport", passport)
                        return headers
                    }
                }

                chain.filter(
                    exchange.mutate()
                        .request(decoratedRequest)
                        .build()
                )
            }
            .switchIfEmpty(chain.filter(exchange))
    }

    private fun extractPassport(jwt: String?): Mono<String> {
        if (jwt.isNullOrBlank()) return exchangePassport()

        return repository.get(jwt)
            .switchIfEmpty(
                exchangePassport(jwt)
                    .flatMap { passport ->
                        repository.set(jwt, passport, Duration.ofMinutes(5))
                            .thenReturn(passport)
                    }
            )
    }

    private fun exchangePassport(jwt: String? = null): Mono<String> {
        return webClient.post()
            .uri("/passport")
            .headers { headers -> jwt?.takeIf { it.isNotBlank() }?.let { headers.setBearerAuth(it) } }
            .exchangeToMono(::extractPassportFromResponse)
    }

    private fun extractPassportFromResponse(response: ClientResponse): Mono<String> {
        val status = response.statusCode().value()

        return response.bodyToMono<WebClientApiResponse<ExchangePassportResponse>>()
            .defaultIfEmpty(WebClientApiResponse(status = status, message = "empty response"))
            .map(::extractPassportOrThrow)
    }

    private fun extractPassportOrThrow(wrapper: WebClientApiResponse<ExchangePassportResponse>): String {
        when {
            wrapper.code == AuthTokenExceptionCode.TOKEN_EXPIRED.name -> throw TokenExpiredException()
            wrapper.is4xxError() -> throw InvalidTokenSignatureException()
            wrapper.is5xxError() -> throw BaseInternalServerException()
        }

        return wrapper.data?.passport ?: throw BaseInternalServerException()
    }
}