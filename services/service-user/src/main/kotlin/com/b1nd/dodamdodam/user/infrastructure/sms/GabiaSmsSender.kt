package com.b1nd.dodamdodam.user.infrastructure.sms

import com.b1nd.dodamdodam.core.common.exception.base.BaseInternalServerException
import com.b1nd.dodamdodam.user.infrastructure.sms.data.GabiaTokenResponse
import com.b1nd.dodamdodam.user.infrastructure.sms.properties.GabiaSmsProperties
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.Base64
import java.util.concurrent.TimeUnit

@Component
class GabiaSmsSender(
    private val properties: GabiaSmsProperties
) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .build()

    private val objectMapper = jacksonObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    private val apiAuthorizationHeader = createBasicHeader(properties.id, properties.apiKey)
    private val normalizedSenderNumber = normalizePhoneNumber(properties.senderNumber)

    @Volatile
    private var smsAuthorizationHeader: String? = null

    @Volatile
    private var tokenExpiresAt: Instant = Instant.EPOCH

    fun send(phone: String, message: String) {
        val url = resolveSendUrl(message)
        try {
            sendSms(url, phone, message, getOrIssueSmsAuthorizationHeader())
        } catch (ex: SmsApiException) {
            if (!ex.isInvalidToken) throw ex
            invalidateToken()
            sendSms(url, phone, message, getOrIssueSmsAuthorizationHeader())
        }
    }

    @Synchronized
    private fun getOrIssueSmsAuthorizationHeader(): String {
        if (hasValidToken()) {
            return smsAuthorizationHeader!!
        }

        val response = requestAccessToken()
        updateTokenCache(response)
        return smsAuthorizationHeader!!
    }

    private fun hasValidToken(): Boolean =
        smsAuthorizationHeader != null && Instant.now().isBefore(tokenExpiresAt)

    private fun requestAccessToken(): GabiaTokenResponse {
        val body = FormBody.Builder()
            .add(GRANT_TYPE_KEY, CLIENT_CREDENTIALS)
            .build()

        val request = Request.Builder()
            .url(properties.tokenUrl)
            .post(body)
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .addHeader("Authorization", apiAuthorizationHeader)
            .build()

        client.newCall(request).execute().use { response ->
            val responseBody = response.body?.string() ?: throw BaseInternalServerException()
            if (!response.isSuccessful) throw BaseInternalServerException()
            return objectMapper.readValue(responseBody)
        }
    }

    private fun updateTokenCache(response: GabiaTokenResponse) {
        smsAuthorizationHeader = createBasicHeader(properties.id, response.accessToken)
        val expiresIn = response.expiresIn ?: DEFAULT_TOKEN_EXPIRES_IN_SECONDS
        tokenExpiresAt = Instant.now().plusSeconds((expiresIn - TOKEN_BUFFER_SECONDS).coerceAtLeast(MIN_TOKEN_TTL_SECONDS))
    }

    private fun resolveSendUrl(message: String): String {
        val isLms = message.toByteArray(StandardCharsets.UTF_8).size > SMS_MAX_LENGTH
        return if (isLms) properties.lmsUrl else properties.smsUrl
    }

    private fun sendSms(url: String, phone: String, message: String, smsAuthHeader: String) {
        val bodyBuilder = FormBody.Builder()
            .add(PHONE_KEY, normalizePhoneNumber(phone))
            .add(CALLBACK_KEY, normalizedSenderNumber)
            .add(MESSAGE_KEY, message)

        if (properties.refKey.isNotBlank()) {
            bodyBuilder.add(REF_KEY, properties.refKey)
        }
        if (properties.subject.isNotBlank()) {
            bodyBuilder.add(SUBJECT_KEY, properties.subject)
        }

        val request = Request.Builder()
            .url(url)
            .post(bodyBuilder.build())
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .addHeader("Authorization", smsAuthHeader)
            .addHeader("cache-control", "no-cache")
            .build()

        client.newCall(request).execute().use { response ->
            if (response.code == 400) {
                val responseBody = response.body?.string() ?: ""
                throw SmsApiException(responseBody.contains(INVALID_TOKEN_CODE, ignoreCase = true), responseBody)
            }
        }
    }

    @Synchronized
    private fun invalidateToken() {
        smsAuthorizationHeader = null
        tokenExpiresAt = Instant.EPOCH
    }

    private fun createBasicHeader(id: String, secret: String): String {
        val encoded = Base64.getEncoder()
            .encodeToString("$id:$secret".toByteArray(StandardCharsets.UTF_8))
        return "Basic $encoded"
    }

    private fun normalizePhoneNumber(raw: String): String =
        raw.filter { it.isDigit() }

    private class SmsApiException(val isInvalidToken: Boolean, message: String) : RuntimeException(message)

    companion object {
        private const val TIMEOUT_SECONDS = 5L
        private const val GRANT_TYPE_KEY = "grant_type"
        private const val CLIENT_CREDENTIALS = "client_credentials"
        private const val PHONE_KEY = "phone"
        private const val CALLBACK_KEY = "callback"
        private const val MESSAGE_KEY = "message"
        private const val REF_KEY = "refkey"
        private const val SUBJECT_KEY = "subject"
        private const val SMS_MAX_LENGTH = 89
        private const val DEFAULT_TOKEN_EXPIRES_IN_SECONDS = 3_600L
        private const val TOKEN_BUFFER_SECONDS = 60L
        private const val MIN_TOKEN_TTL_SECONDS = 30L
        private const val INVALID_TOKEN_CODE = "invalid_token"
    }
}
