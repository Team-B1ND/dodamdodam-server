package com.b1nd.dodamdodam.banner.application.banner.data.response

import com.b1nd.dodamdodam.banner.domain.banner.entity.BannerEntity
import com.b1nd.dodamdodam.banner.domain.banner.enumeration.BannerStatus
import java.time.LocalDateTime

data class BannerResponse(
    val id: Long,
    val title: String,
    val imageUrl: String,
    val redirectUrl: String,
    val status: BannerStatus,
    val expireAt: LocalDateTime,
) {
    companion object {
        fun fromEntity(banner: BannerEntity): BannerResponse =
            BannerResponse(
                id = banner.id!!,
                title = banner.title,
                imageUrl = banner.imageUrl,
                redirectUrl = banner.redirectUrl,
                status = banner.status,
                expireAt = banner.expireAt,
            )
    }
}
