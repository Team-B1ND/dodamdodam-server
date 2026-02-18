package com.b1nd.dodamdodam.core.security.jwt.util

import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

object KeyLoader {
    fun loadPrivateKey(pem: String): RSAPrivateKey {
        val key = replaceKey(pem, "PRIVATE")
        val decoded = Base64.getDecoder().decode(key)
        val spec = PKCS8EncodedKeySpec(decoded)
        val factory = KeyFactory.getInstance("RSA")
        return factory.generatePrivate(spec) as RSAPrivateKey
    }

    fun loadPublicKey(pem: String): RSAPublicKey {
        val key = replaceKey(pem, "PUBLIC")

        val decoded = Base64.getDecoder().decode(key)
        val spec = X509EncodedKeySpec(decoded)
        val factory = KeyFactory.getInstance("RSA")
        return factory.generatePublic(spec) as RSAPublicKey
    }

    private fun replaceKey(key: String, type: String) = key
        .replace("-----BEGIN $type KEY-----", "")
        .replace("-----END $type KEY-----", "")
        .replace("\\s".toRegex(), "")
}