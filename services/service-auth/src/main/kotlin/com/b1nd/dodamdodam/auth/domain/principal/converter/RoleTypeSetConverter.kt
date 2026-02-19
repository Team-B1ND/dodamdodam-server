package com.b1nd.dodamdodam.auth.domain.principal.converter

import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class RoleTypeSetConverter: AttributeConverter<Set<RoleType>, String> {
    override fun convertToDatabaseColumn(attribute: Set<RoleType>?): String {
        if (attribute.isNullOrEmpty()) return ""
        return attribute.joinToString(",") { it.name }
    }

    override fun convertToEntityAttribute(dbData: String?): Set<RoleType> {
        if (dbData.isNullOrBlank()) return emptySet()
        return dbData.split(",")
            .map { it.trim() }
            .mapNotNull { name ->
                runCatching { RoleType.valueOf(name) }.getOrNull()
            }
            .toSet()
    }
}