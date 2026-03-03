package com.b1nd.dodamdodam.inapp

import com.b1nd.dodamdodam.core.common.swagger.annotation.EnableDodamSwagger
import com.b1nd.dodamdodam.core.kafka.annotation.EnableDodamKafka
import com.b1nd.dodamdodam.core.redis.annotation.EnableDodamRedis
import com.b1nd.dodamdodam.core.security.annotation.EnableDodamSecurity
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication(scanBasePackages = ["com.b1nd.dodamdodam"])
@EnableDodamKafka
@EnableDodamRedis
@EnableJpaAuditing
@EnableDodamSecurity
@ConfigurationPropertiesScan
@EnableDodamSwagger
class InAppServiceApplication

fun main(args: Array<String>) {
    runApplication<InAppServiceApplication>(*args)
}
