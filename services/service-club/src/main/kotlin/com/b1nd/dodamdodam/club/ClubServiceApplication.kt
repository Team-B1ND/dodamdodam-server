package com.b1nd.dodamdodam.club

import com.b1nd.dodamdodam.core.security.annotation.EnableDodamSecurity
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@SpringBootApplication(scanBasePackages = ["com.b1nd.dodamdodam"])
@EnableDodamSecurity
@EnableWebSecurity
@ConfigurationPropertiesScan
class ClubServiceApplication

fun main(args: Array<String>) {
    runApplication<ClubServiceApplication>(*args)
}
