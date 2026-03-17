package com.b1nd.dodamdodam.inapp.infrastructure.kafka.producer

import com.b1nd.dodamdodam.core.kafka.constants.KafkaTopics
import com.b1nd.dodamdodam.core.kafka.event.app.AppReleaseActivatedEvent
import com.b1nd.dodamdodam.core.kafka.producer.KafkaMessageProducer
import com.b1nd.dodamdodam.inapp.domain.app.entity.AppReleaseEntity
import org.springframework.stereotype.Component

@Component
class AppReleaseActivatedEventProducer(
    private val kafkaMessageProducer: KafkaMessageProducer
) {
    fun publishActivated(release: AppReleaseEntity) {
        kafkaMessageProducer.send(
            KafkaTopics.APP_RELEASE_ACTIVATED,
            release.app.publicId.toString(),
            AppReleaseActivatedEvent(
                appPublicId = release.app.publicId!!,
                releasePublicId = release.publicId!!,
                appName = release.app.name,
                githubReleaseUrl = release.releaseUrl,
            )
        )
    }
}
