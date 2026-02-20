package com.b1nd.dodamdodam.banner.application.banner.data

import com.b1nd.dodamdodam.banner.application.banner.data.request.BannerRequest
import com.b1nd.dodamdodam.banner.domain.banner.entity.BannerEntity
import com.b1nd.dodamdodam.banner.domain.banner.enumeration.BannerStatus
import java.time.LocalDateTime

fun BannerRequest.toBannerEntity(): BannerEntity =
    BannerEntity(
        title = title,
        imageUrl = image,
        redirectUrl = url,
        status = if (LocalDateTime.now().isBefore(expireAt)) BannerStatus.ACTIVE else BannerStatus.DEACTIVATED,
        expireAt = expireAt,
    )
