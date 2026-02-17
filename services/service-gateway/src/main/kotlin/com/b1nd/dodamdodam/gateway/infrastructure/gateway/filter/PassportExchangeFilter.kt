package com.b1nd.dodamdodam.gateway.infrastructure.gateway.filter

import com.b1nd.dodamdodam.gateway.domain.passport.repository.PassportCacheRepository
import com.b1nd.dodamdodam.gateway.domain.passport.service.PassportService
import com.b1nd.dodamdodam.gateway.infrastructure.auth.client.AuthClient
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class PassportExchangeFilter(
    private val authClient: AuthClient,
    private val service: PassportService,
    private val prefix: String = "Bearer "
): GlobalFilter {
    override fun filter(
        exchange: ServerWebExchange,
        chain: GatewayFilterChain
    ): Mono<Void>  = mono {
        val authHeader: String? = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)
        val jwt: String? = authHeader?.removePrefix(prefix)?.trim()
        val passport = extractPassport(jwt)

        val mutatedRequest =
            exchange.request.mutate()
                .header("X-User-Passport", passport)
                .build()

        chain.filter(
            exchange.mutate()
                .request(mutatedRequest)
                .build()
        ).awaitSingleOrNull()
    }

    private suspend fun extractPassport(jwt: String?): String {
        val passport = jwt?.let { service.find(it) }
            ?: authClient.exchangePassport(jwt)
        jwt?.let { service.save(it, passport) }
        return passport
    }
}