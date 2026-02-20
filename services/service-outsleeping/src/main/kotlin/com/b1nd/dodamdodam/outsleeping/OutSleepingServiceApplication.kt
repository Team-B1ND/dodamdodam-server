package com.b1nd.dodamdodam.outsleeping

import com.b1nd.dodamdodam.core.security.annotation.EnableDodamSecurity
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@SpringBootApplication(scanBasePackages = ["com.b1nd.dodamdodam"])
@EnableDodamSecurity
@EnableWebSecurity
@EnableJpaAuditing
class OutSleepingServiceApplication

fun main(args: Array<String>) {
    runApplication<OutSleepingServiceApplication>(*args)
}
