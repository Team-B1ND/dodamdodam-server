package com.b1nd.dodamdodam.auth.domain.principal.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "principal_refresh_tokens")
class PrincipalRefreshTokenEntity(
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "fk_principal_id",
        nullable = false
    )
    val principal: PrincipalEntity,
    @Column(nullable = false, columnDefinition = "TEXT")
    val token: String,
    @Column(nullable = true)
    val userAgent: String? = null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}