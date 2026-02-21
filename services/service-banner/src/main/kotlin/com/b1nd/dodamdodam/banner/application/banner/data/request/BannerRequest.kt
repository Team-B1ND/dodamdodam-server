package com.b1nd.dodamdodam.banner.application.banner.data.request

import java.time.LocalDateTime

data class BannerRequest(
    val title: String,
    val image: String,
    val url: String,
    val expireAt: LocalDateTime,
)
