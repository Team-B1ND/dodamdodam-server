package com.b1nd.dodamdodam.inapp.domain.app.entity

import com.b1nd.dodamdodam.inapp.domain.app.enumeration.AppStatusType
import com.b1nd.dodamdodam.inapp.domain.team.entity.TeamEntity
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
@Table(name = "apps")
class AppEntity(
    var name: String,
    var description: String? = null,
    var subtitle: String,
    var iconUrl: String,
    var darkIconUrl: String? = null,
    var inquiryMail: String,

    var releaseEnabled: Boolean? = null,

    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    var releaseStatus: AppStatusType? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_team_id")
    val team: TeamEntity,
) {
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

    fun update(
        name: String?,
        subtitle: String?,
        description: String?,
        iconUrl: String?,
        darkIconUrl: String?,
        inquiryMail: String?
    ) {
        name?.let { this.name = it }
        subtitle?.let { this.subtitle = it }
        description?.let { this.description = it }
        iconUrl?.let { this.iconUrl = it }
        darkIconUrl?.let { this.darkIconUrl = it }
        inquiryMail?.let { this.inquiryMail = it }
    }

    fun updateReleaseInfo(enabled: Boolean, status: AppStatusType) {
        this.releaseEnabled = enabled
        this.releaseStatus = status
    }
}