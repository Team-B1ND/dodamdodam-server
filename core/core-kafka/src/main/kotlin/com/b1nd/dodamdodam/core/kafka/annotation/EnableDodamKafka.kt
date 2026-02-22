package com.b1nd.dodamdodam.core.kafka.annotation

import com.b1nd.dodamdodam.core.kafka.config.KafkaConfig
import org.springframework.context.annotation.Import

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Import(KafkaConfig::class)
annotation class EnableDodamKafka
