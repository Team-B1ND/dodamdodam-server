package com.b1nd.dodamdodam.member.domain.member.entity

import com.b1nd.dodamdodam.core.jpa.entity.BaseTimeEntity
import com.b1nd.dodamdodam.member.domain.member.enumeration.ActiveStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "members")
class MemberEntity(
    @Column(nullable = false, unique = true)
    val username: String,

    @Column(nullable = false)
    var password: String,

    @Column(nullable = false)
    var name: String,

    var email: String? = null,

    @Column(nullable = false)
    var phone: String,

    @Column(columnDefinition = "TEXT")
    var profileImage: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: ActiveStatus = ActiveStatus.PENDING,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun updatePassword(newPassword: String) {
        this.password = newPassword
    }

    fun updateInfo(name: String?, email: String?, phone: String?, profileImage: String?) {
        name?.takeIf { it.isNotBlank() }?.let { this.name = it }
        email?.takeIf { it.isNotBlank() }?.let { this.email = it }
        phone?.takeIf { it.isNotBlank() }?.let { this.phone = it }
        this.profileImage = profileImage
    }

    fun updateStatus(status: ActiveStatus) {
        this.status = status
    }
}
