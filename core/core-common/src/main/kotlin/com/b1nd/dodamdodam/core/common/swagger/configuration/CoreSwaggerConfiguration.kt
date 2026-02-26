package com.b1nd.dodamdodam.core.common.swagger.configuration

import com.b1nd.dodamdodam.core.common.swagger.properties.SwaggerProperties
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.servers.Server
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(SwaggerProperties::class)
class CoreSwaggerConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = "app.swagger", name = ["enabled"], havingValue = "true")
    @ConditionalOnMissingBean(OpenAPI::class)
    fun openApi(swaggerProperties: SwaggerProperties): OpenAPI =
        OpenAPI().servers(listOf(Server().url(swaggerProperties.gatewayPrefix)))
}
