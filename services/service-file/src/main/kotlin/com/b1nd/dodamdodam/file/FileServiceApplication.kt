package com.b1nd.dodamdodam.file

import com.b1nd.dodamdodam.core.common.swagger.annotation.EnableDodamSwagger
import com.b1nd.dodamdodam.core.security.annotation.EnableDodamSecurity
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.b1nd.dodamdodam"])
@EnableDodamSecurity
@ConfigurationPropertiesScan
@EnableDodamSwagger
class FileServiceApplication

fun main(args: Array<String>) {
    runApplication<FileServiceApplication>(*args)
}
