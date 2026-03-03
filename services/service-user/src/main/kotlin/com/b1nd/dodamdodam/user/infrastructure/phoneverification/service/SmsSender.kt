package com.b1nd.dodamdodam.user.infrastructure.phoneverification.service

interface SmsSender {
    fun send(phone: String, message: String)
}
