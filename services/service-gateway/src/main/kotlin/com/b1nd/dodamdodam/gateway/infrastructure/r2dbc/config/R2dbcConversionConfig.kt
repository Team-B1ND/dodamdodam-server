package com.b1nd.dodamdodam.gateway.infrastructure.r2dbc.config

import com.b1nd.dodamdodam.gateway.infrastructure.r2dbc.converter.StringToUuidConverter
import com.b1nd.dodamdodam.gateway.infrastructure.r2dbc.converter.UuidToStringConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions
import org.springframework.data.r2dbc.dialect.MySqlDialect

@Configuration
class R2dbcConversionConfig {
    @Bean
    fun r2dbcCustomConversions(): R2dbcCustomConversions =
        R2dbcCustomConversions.of(
            MySqlDialect.INSTANCE,
            listOf(
                UuidToStringConverter(),
                StringToUuidConverter()
            )
        )
}
