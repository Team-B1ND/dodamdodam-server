package com.b1nd.dodamdodam.inapp.domain.app.entity

import com.b1nd.dodamdodam.core.jpa.entity.BaseTimeEntity
import com.b1nd.dodamdodam.inapp.domain.app.enumeration.AppStatusType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.PrePersist
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "app_releases")
class AppReleaseEntity(
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_app_id", nullable = false)
    val app: AppEntity,

    var enabled: Boolean,
    @Column(nullable = false, columnDefinition = "TEXT")
    val releaseUrl: String,
    @Column(name = "fk_user_id")
    var updatedUser: UUID,
    @Column(columnDefinition = "TEXT")
    var memo: String? = null,
    @Column(columnDefinition = "TEXT")
    var denyResult: String? = null,
    @Enumerated(EnumType.STRING)
    var status: AppStatusType
): BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(nullable = false, unique = true)
    var publicId: UUID? = null
        protected set

    @PrePersist
    fun generatePublicId() {
        publicId = UUID.randomUUID()
    }

    fun updateStatus(status: AppStatusType, denyResult: String?, updatedUser: UUID) {
        this.status = status
        this.denyResult = denyResult
        this.updatedUser = updatedUser
        if (status != AppStatusType.ALLOWED) {
            this.enabled = false
        }
    }

    fun updateEnabled(enabled: Boolean, updatedUser: UUID) {
        this.enabled = enabled
        this.updatedUser = updatedUser
    }
}
