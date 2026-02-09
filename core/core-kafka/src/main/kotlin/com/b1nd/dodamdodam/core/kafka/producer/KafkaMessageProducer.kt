package com.b1nd.dodamdodam.core.kafka.producer

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaMessageProducer(
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {
    fun send(topic: String, message: Any) {
        kafkaTemplate.send(topic, message)
    }

    fun send(topic: String, key: String, message: Any) {
        kafkaTemplate.send(topic, key, message)
    }
}
