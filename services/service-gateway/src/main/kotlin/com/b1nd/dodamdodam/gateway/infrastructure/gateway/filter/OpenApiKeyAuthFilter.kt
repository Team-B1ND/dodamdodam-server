package com.b1nd.dodamdodam.gateway.infrastructure.gateway.filter

import com.b1nd.dodamdodam.core.common.data.webclient.WebClientApiResponse
import com.b1nd.dodamdodam.core.common.exception.base.BaseForbiddenException
import com.b1nd.dodamdodam.core.common.exception.base.BaseUnauthorizedException
import com.b1nd.dodamdodam.gateway.domain.openapi.repository.OpenApiKeyCacheRepository
import com.b1nd.dodamdodam.gateway.infrastructure.auth.properties.AuthProperties
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.core.Ordered
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.time.Duration

@Component
class OpenApiKeyAuthFilter(
    private val cacheRepository: OpenApiKeyCacheRepository,
    private val webClientBuilder: WebClient.Builder,
    private val authProperties: AuthProperties
) : GlobalFilter, Ordered {
    private val webClient by lazy { webClientBuilder.baseUrl(authProperties.url).build() }

    companion object {
        private val CACHE_TTL = Duration.ofDays(90)
    }

    override fun getOrder(): Int = -1

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val path = exchange.request.uri.path
        if (!path.startsWith("/openapi/")) {
            return chain.filter(exchange)
        }

        val appId = exchange.request.headers.getFirst("X-App-Id")
            ?: return Mono.error(BaseUnauthorizedException())
        val apiKey = exchange.request.headers.getFirst("X-Api-Key")
            ?: return Mono.error(BaseUnauthorizedException())

        return cacheRepository.get(appId)
            .flatMap { cached ->
                if (cached == apiKey) {
                    chain.filter(exchange)
                } else {
                    verifyViaAuth(appId, apiKey, exchange, chain)
                }
            }
            .switchIfEmpty(verifyViaAuth(appId, apiKey, exchange, chain))
    }

    private fun verifyViaAuth(
        appId: String,
        apiKey: String,
        exchange: ServerWebExchange,
        chain: GatewayFilterChain
    ): Mono<Void> {
        return webClient.get()
            .uri { it.path("/open-api/verify").queryParam("appId", appId).queryParam("apiKey", apiKey).build() }
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<WebClientApiResponse<Map<String, Boolean>>>() {})
            .flatMap { response ->
                val isValid = response.data?.get("valid") == true
                if (isValid) {
                    cacheRepository.set(appId, apiKey, CACHE_TTL)
                        .then(chain.filter(exchange))
                } else {
                    Mono.error(BaseForbiddenException())
                }
            }
            .onErrorResume { ex ->
                when (ex) {
                    is BaseForbiddenException, is BaseUnauthorizedException -> Mono.error(ex)
                    else -> Mono.error(BaseForbiddenException())
                }
            }
    }
}