package com.b1nd.dodamdodam.user.infrastructure.sms

import com.b1nd.dodamdodam.core.common.exception.base.BaseInternalServerException
import com.b1nd.dodamdodam.user.infrastructure.sms.data.GabiaTokenResponse
import com.b1nd.dodamdodam.user.infrastructure.sms.properties.GabiaSmsProperties
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.HttpClientErrorException
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestClient
import java.time.Instant
import java.nio.charset.StandardCharsets
import java.util.Base64

@Component
class GabiaSmsSender(
    private val properties: GabiaSmsProperties
) {
    private val restClient = RestClient.builder()
        .requestFactory(SimpleClientHttpRequestFactory().apply {
            setConnectTimeout(TIMEOUT_MILLIS)
            setReadTimeout(TIMEOUT_MILLIS)
        })
        .build()

    private val apiAuthorizationHeader = createBasicHeader(properties.id, properties.apiKey)
    private val normalizedSenderNumber = normalizePhoneNumber(properties.senderNumber)

    @Volatile
    private var smsAuthorizationHeader: String? = null

    @Volatile
    private var tokenExpiresAt: Instant = Instant.EPOCH

    fun send(phone: String, message: String) {
        val formData = createSendFormData(phone, message)
        val url = resolveSendUrl(message)
        try {
            sendSms(url, formData, getOrIssueSmsAuthorizationHeader())
        } catch (ex: HttpClientErrorException.BadRequest) {
            if (!isInvalidTokenError(ex)) {
                throw ex
            }
            invalidateToken()
            sendSms(url, formData, getOrIssueSmsAuthorizationHeader())
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
        val form = LinkedMultiValueMap<String, String>()
        form.add(GRANT_TYPE_KEY, CLIENT_CREDENTIALS)

        return restClient.post()
            .uri(properties.tokenUrl)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header(HttpHeaders.AUTHORIZATION, apiAuthorizationHeader)
            .body(form)
            .retrieve()
            .body(GabiaTokenResponse::class.java)
            ?: throw BaseInternalServerException()
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

    private fun createSendFormData(phone: String, message: String): LinkedMultiValueMap<String, String> {
        val formData = LinkedMultiValueMap<String, String>()
        formData.add(PHONE_KEY, normalizePhoneNumber(phone))
        formData.add(CALLBACK_KEY, normalizedSenderNumber)
        formData.add(MESSAGE_KEY, message)
        if (properties.refKey.isNotBlank()) {
            formData.add(REF_KEY, properties.refKey)
        }
        if (properties.subject.isNotBlank()) {
            formData.add(SUBJECT_KEY, properties.subject)
        }
        return formData
    }

    private fun sendSms(url: String, formData: LinkedMultiValueMap<String, String>, smsAuthHeader: String) {
        restClient.post()
            .uri(url)
            .header(HttpHeaders.AUTHORIZATION, smsAuthHeader)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(formData)
            .retrieve()
            .toBodilessEntity()
    }

    @Synchronized
    private fun invalidateToken() {
        smsAuthorizationHeader = null
        tokenExpiresAt = Instant.EPOCH
    }

    private fun isInvalidTokenError(ex: HttpClientErrorException.BadRequest): Boolean =
        ex.responseBodyAsString.contains(INVALID_TOKEN_CODE, ignoreCase = true)

    private fun createBasicHeader(id: String, secret: String): String {
        val encoded = Base64.getEncoder()
            .encodeToString("$id:$secret".toByteArray(StandardCharsets.UTF_8))
        return "Basic $encoded"
    }

    private fun normalizePhoneNumber(raw: String): String =
        raw.filter { it.isDigit() }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000
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