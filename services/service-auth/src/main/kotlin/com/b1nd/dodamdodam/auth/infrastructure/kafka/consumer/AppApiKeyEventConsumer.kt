package com.b1nd.dodamdodam.auth.infrastructure.kafka.consumer

import com.b1nd.dodamdodam.auth.infrastructure.openapi.repository.OpenApiKeyCacheRepository
import com.b1nd.dodamdodam.core.kafka.constants.KafkaTopics
import com.b1nd.dodamdodam.core.kafka.event.app.AppApiKeyCreatedEvent
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class AppApiKeyEventConsumer(
    private val openApiKeyCacheRepository: OpenApiKeyCacheRepository
) {
    companion object {
        private val API_KEY_CACHE_TTL = Duration.ofDays(90)
    }

    @KafkaListener(
        topics = [KafkaTopics.APP_API_KEY_CREATED],
        groupId = "service-auth-api-key",
        containerFactory = "kafkaListenerContainerFactory"
    )
    fun consumeApiKeyCreated(event: AppApiKeyCreatedEvent) {
        openApiKeyCacheRepository.set(
            appPublicId = event.appPublicId.toString(),
            apiKey = event.apiKey,
            ttl = API_KEY_CACHE_TTL
        )
    }
}