package com.b1nd.dodamdodam.gateway.domain.passport.service

import com.b1nd.dodamdodam.gateway.domain.passport.repository.PassportCacheRepository
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class PassportService(
    private val repository: PassportCacheRepository
) {
    suspend fun find(key: String): String? {
        return repository.get(key)
    }

    suspend fun save(key: String, value: String) {
        repository.set(key, value, Duration.ofMinutes(5))
    }
}