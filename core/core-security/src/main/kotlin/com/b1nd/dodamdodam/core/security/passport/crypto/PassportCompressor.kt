package com.b1nd.dodamdodam.core.security.passport.crypto

import com.b1nd.dodamdodam.core.security.exception.PassportDecompressionException
import com.b1nd.dodamdodam.core.security.exception.PassportSerializationException
import com.b1nd.dodamdodam.core.security.passport.Passport
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import java.util.Base64
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

object PassportCompressor {
    private val objectMapper: ObjectMapper = jacksonObjectMapper()

    fun compress(passport: Passport): String {
        return try {
            val json = objectMapper.writeValueAsString(passport)
            val byteStream = ByteArrayOutputStream()
            GZIPOutputStream(byteStream).use { gzip ->
                gzip.write(json.toByteArray(StandardCharsets.UTF_8))
            }
            Base64.getEncoder().encodeToString(byteStream.toByteArray())
        } catch (_: Exception) {
            throw PassportSerializationException()
        }
    }

    fun decompress(base64Payload: String): Passport {
        return try {
            val compressedBytes = Base64.getDecoder().decode(base64Payload)
            val json = GZIPInputStream(ByteArrayInputStream(compressedBytes)).use { gzip ->
                gzip.readBytes().toString(StandardCharsets.UTF_8)
            }
            objectMapper.readValue(json, Passport::class.java)
        } catch (_: Exception) {
            throw PassportDecompressionException()
        }
    }
}