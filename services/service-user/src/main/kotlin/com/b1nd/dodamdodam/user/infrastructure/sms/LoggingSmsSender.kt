package com.b1nd.dodamdodam.user.infrastructure.sms

import com.b1nd.dodamdodam.user.infrastructure.phoneverification.service.SmsSender
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class LoggingSmsSender: SmsSender {
    private val logger = LoggerFactory.getLogger(LoggingSmsSender::class.java)

    override fun send(phone: String, message: String) {
        logger.info("SMS sent to {}: {}", phone, message)
    }
}
