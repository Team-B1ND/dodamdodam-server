package com.b1nd.dodamdodam.inapp.domain.app.entity

import com.b1nd.dodamdodam.core.jpa.entity.BaseTimeEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.persistence.Transient
import java.time.LocalDateTime

@Entity
@Table(name = "app_api_keys")
class AppApiKeyEntity(
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_app_id", unique = true)
    val app: AppEntity,

    @Column(name = "api_key", nullable = false)
    var apiKey: String,

    @Column(nullable = false)
    var expiredAt: LocalDateTime,

    @Transient
    val rawApiKey: String? = null,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    val isExpired: Boolean
        get() = LocalDateTime.now().isAfter(expiredAt)

    fun updateApiKey(newHash: String, newExpiredAt: LocalDateTime) {
        this.apiKey = newHash
        this.expiredAt = newExpiredAt
    }
}