package com.b1nd.dodamdodam.auth

import com.b1nd.dodamdodam.core.security.annotation.EnableDodamSecurity
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.b1nd.dodamdodam"])
@EnableDodamSecurity
@ConfigurationPropertiesScan
class AuthServiceApplication

fun main(args: Array<String>) {
    runApplication<AuthServiceApplication>(*args)
}
