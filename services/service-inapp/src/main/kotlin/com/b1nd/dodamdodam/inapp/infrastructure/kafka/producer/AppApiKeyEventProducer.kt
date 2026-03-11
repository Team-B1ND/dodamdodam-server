package com.b1nd.dodamdodam.inapp.infrastructure.kafka.producer

import com.b1nd.dodamdodam.core.kafka.constants.KafkaTopics
import com.b1nd.dodamdodam.core.kafka.event.app.AppApiKeyCreatedEvent
import com.b1nd.dodamdodam.core.kafka.producer.KafkaMessageProducer
import com.b1nd.dodamdodam.inapp.domain.app.entity.AppApiKeyEntity
import org.springframework.stereotype.Component

@Component
class AppApiKeyEventProducer(
    private val kafkaMessageProducer: KafkaMessageProducer
) {
    fun publishCreated(apiKeyEntity: AppApiKeyEntity) {
        kafkaMessageProducer.send(
            KafkaTopics.APP_API_KEY_CREATED,
            apiKeyEntity.app.publicId.toString(),
            AppApiKeyCreatedEvent(
                appPublicId = apiKeyEntity.app.publicId!!,
                apiKey = apiKeyEntity.apiKey,
            )
        )
    }
}
