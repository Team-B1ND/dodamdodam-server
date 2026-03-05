package com.b1nd.dodamdodam.gateway

import com.b1nd.dodamdodam.core.kafka.annotation.EnableDodamKafka
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication(scanBasePackages = ["com.b1nd.dodamdodam"])
@EnableDodamKafka
@EnableScheduling
@ConfigurationPropertiesScan
class GatewayServiceApplication

fun main(args: Array<String>) {
    runApplication<GatewayServiceApplication>(*args)
}
