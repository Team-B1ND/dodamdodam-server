package com.b1nd.dodamdodam.banner.domain.banner.entity

import com.b1nd.dodamdodam.banner.domain.banner.enumeration.BannerStatus
import com.b1nd.dodamdodam.core.jpa.entity.BaseTimeEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "banners")
class BannerEntity(
    @Column(nullable = false)
    var title: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    var imageUrl: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    var redirectUrl: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: BannerStatus,

    @Column(nullable = false)
    var expireAt: LocalDateTime,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun updateBanner(title: String, imageUrl: String, redirectUrl: String, expireAt: LocalDateTime) {
        this.title = title
        this.imageUrl = imageUrl
        this.redirectUrl = redirectUrl
        this.expireAt = expireAt
    }

    fun activateStatus() {
        this.status = BannerStatus.ACTIVE
    }

    fun deactivateStatus() {
        this.status = BannerStatus.DEACTIVATED
    }
}
