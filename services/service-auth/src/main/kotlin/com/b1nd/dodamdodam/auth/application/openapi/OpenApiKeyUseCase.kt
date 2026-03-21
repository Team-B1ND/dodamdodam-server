package com.b1nd.dodamdodam.auth.application.openapi

import com.b1nd.dodamdodam.auth.infrastructure.openapi.client.InAppClient
import com.b1nd.dodamdodam.auth.infrastructure.openapi.repository.OpenApiKeyCacheRepository
import kotlinx.coroutines.runBlocking
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class OpenApiKeyUseCase(
    private val cacheRepository: OpenApiKeyCacheRepository,
    private val inAppClient: InAppClient,
    private val passwordEncoder: BCryptPasswordEncoder
) {
    companion object {
        private val API_KEY_CACHE_TTL = Duration.ofDays(90)
    }

    fun verify(appPublicId: String, rawApiKey: String): Boolean {
        val cachedHash = cacheRepository.get(appPublicId)
        if (cachedHash != null) {
            return passwordEncoder.matches(rawApiKey, cachedHash)
        }

        val isValid = runBlocking { inAppClient.verifyApiKey(appPublicId, rawApiKey) }
        if (isValid) {
            cacheRepository.set(appPublicId, passwordEncoder.encode(rawApiKey), API_KEY_CACHE_TTL)
        }
        return isValid
    }
}