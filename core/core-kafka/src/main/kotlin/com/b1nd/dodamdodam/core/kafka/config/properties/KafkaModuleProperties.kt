package com.b1nd.dodamdodam.core.kafka.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.kafka")
data class KafkaModuleProperties(
    val bootstrapServers: String = "localhost:9092",
    val consumerGroupId: String = "dodamdodam",
    val autoOffsetReset: String = "latest"
)
