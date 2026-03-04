package com.b1nd.dodamdodam.inapp.domain.app.entity

import com.b1nd.dodamdodam.core.jpa.entity.BaseTimeEntity
import com.b1nd.dodamdodam.inapp.domain.app.enumeration.AppReleaseStatusType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "app_releases")
class AppReleaseEntity(
    var enabled: Boolean,
    @Column(nullable = false, columnDefinition = "TEXT")
    val releaseUrl: String,
    @Column(name = "fk_user_id")
    val updatedUser: UUID,
    @Column(columnDefinition = "TEXT")
    var memo: String? = null,
    @Column(columnDefinition = "TEXT")
    var denyResult: String? = null,
    @Enumerated(EnumType.STRING)
    var status: AppReleaseStatusType
): BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}