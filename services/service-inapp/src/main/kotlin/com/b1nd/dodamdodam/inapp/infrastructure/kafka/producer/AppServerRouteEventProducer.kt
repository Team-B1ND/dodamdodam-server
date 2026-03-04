package com.b1nd.dodamdodam.inapp.infrastructure.kafka.producer

import com.b1nd.dodamdodam.core.kafka.constants.KafkaTopics
import com.b1nd.dodamdodam.core.kafka.event.app.AppServerRouteEvent
import com.b1nd.dodamdodam.core.kafka.event.app.AppServerRouteEventType
import com.b1nd.dodamdodam.core.kafka.producer.KafkaMessageProducer
import com.b1nd.dodamdodam.inapp.domain.app.entity.AppServerEntity
import org.springframework.stereotype.Component

@Component
class AppServerRouteEventProducer(
    private val kafkaMessageProducer: KafkaMessageProducer
) {
    fun publishCreated(server: AppServerEntity) {
        kafkaMessageProducer.send(
            KafkaTopics.APP_SERVER_ROUTE_CHANGED,
            server.app.publicId.toString(),
            toEvent(server, AppServerRouteEventType.CREATED)
        )
    }

    fun publishUpdated(server: AppServerEntity) {
        kafkaMessageProducer.send(
            KafkaTopics.APP_SERVER_ROUTE_CHANGED,
            server.app.publicId.toString(),
            toEvent(server, AppServerRouteEventType.UPDATED)
        )
    }

    private fun toEvent(server: AppServerEntity, eventType: AppServerRouteEventType) =
        AppServerRouteEvent(
            appId = server.app.publicId!!,
            eventType = eventType,
            path = server.redirectPath,
            targetUri = normalizeTargetUri(server.serverAddress),
            stripPrefix = server.prefixLevel,
            enabled = server.enabled,
        )

    private fun normalizeTargetUri(serverAddress: String): String {
        if (serverAddress.startsWith("http://") || serverAddress.startsWith("https://")) {
            return serverAddress
        }
        return "http://$serverAddress"
    }
}
