package com.b1nd.dodamdodam.wakeupsong

import com.b1nd.dodamdodam.core.common.swagger.annotation.EnableDodamSwagger
import com.b1nd.dodamdodam.core.security.annotation.EnableDodamSecurity
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication(scanBasePackages = ["com.b1nd.dodamdodam"])
@EnableJpaAuditing
@EnableDodamSecurity
@ConfigurationPropertiesScan
@EnableDodamSwagger
class WakeupSongServiceApplication

fun main(args: Array<String>) {
    runApplication<WakeupSongServiceApplication>(*args)
}
