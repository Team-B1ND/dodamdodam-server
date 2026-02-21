package com.b1nd.dodamdodam.core.kafka.config

import com.b1nd.dodamdodam.core.kafka.config.properties.KafkaModuleProperties
import com.b1nd.dodamdodam.core.kafka.producer.KafkaMessageProducer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.listener.DefaultErrorHandler
import org.springframework.kafka.support.converter.StringJsonMessageConverter
import org.springframework.kafka.support.serializer.JsonSerializer
import org.springframework.messaging.converter.MessageConversionException
import org.slf4j.LoggerFactory
import org.springframework.util.backoff.FixedBackOff

@Configuration
@EnableKafka
@EnableConfigurationProperties(KafkaModuleProperties::class)
class KafkaConfig(
    private val properties: KafkaModuleProperties
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    fun producerFactory(): ProducerFactory<String, Any> {
        val config = mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to properties.bootstrapServers,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java
        )
        return DefaultKafkaProducerFactory(config)
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, Any> {
        log.info(
            "Kafka configured: bootstrapServers={}, consumerGroupId={}, autoOffsetReset={}",
            properties.bootstrapServers,
            properties.consumerGroupId,
            properties.autoOffsetReset
        )
        return KafkaTemplate(producerFactory())
    }

    @Bean
    fun kafkaMessageProducer(kafkaTemplate: KafkaTemplate<String, Any>): KafkaMessageProducer =
        KafkaMessageProducer(kafkaTemplate)

    @Bean
    fun consumerFactory(): ConsumerFactory<String, String> {
        val config = mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to properties.bootstrapServers,
            ConsumerConfig.GROUP_ID_CONFIG to properties.consumerGroupId,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to properties.autoOffsetReset,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java
        )
        return DefaultKafkaConsumerFactory(config)
    }

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.consumerFactory = consumerFactory()
        factory.setRecordMessageConverter(StringJsonMessageConverter())
        val errorHandler = DefaultErrorHandler(FixedBackOff(0L, 0L))
        errorHandler.addNotRetryableExceptions(
            MessageConversionException::class.java,
            IllegalArgumentException::class.java
        )
        factory.setCommonErrorHandler(errorHandler)
        return factory
    }
}
