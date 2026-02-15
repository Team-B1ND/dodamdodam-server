package com.b1nd.dodamdodam.core.security.crypto

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import java.util.Base64
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

object PassportCompressor {

    fun compress(json: String): String {
        val byteStream = ByteArrayOutputStream()
        GZIPOutputStream(byteStream).use { gzip ->
            gzip.write(json.toByteArray(StandardCharsets.UTF_8))
        }
        return Base64.getEncoder().encodeToString(byteStream.toByteArray())
    }

    fun decompress(base64Payload: String): String {
        val compressedBytes = Base64.getDecoder().decode(base64Payload)
        GZIPInputStream(ByteArrayInputStream(compressedBytes)).use { gzip ->
            return gzip.readBytes().toString(StandardCharsets.UTF_8)
        }
    }
}