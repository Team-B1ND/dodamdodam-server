package com.b1nd.dodamdodam.gateway.infrastructure.r2dbc.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import java.util.UUID

@WritingConverter
class UuidToStringConverter : Converter<UUID, String> {
    override fun convert(source: UUID): String = source.toString()
}

@ReadingConverter
class StringToUuidConverter : Converter<String, UUID> {
    override fun convert(source: String): UUID = UUID.fromString(source)
}
