package com.b1nd.dodamdodam.user.domain.user.entity

import com.b1nd.dodamdodam.core.jpa.entity.BaseTimeEntity
import com.b1nd.dodamdodam.user.domain.user.enumeration.StatusType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.PrePersist
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "users")
class UserEntity (
    @Column(nullable = false)
    val username: String,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false, length = 255)
    var password: String,

    @Column(columnDefinition = "TEXT")
    var profileImage: String? = null,

    var phone: String? = null,

    var status: StatusType = StatusType.PENDING
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

    fun updatePassword(newPassword: String) {
        this.password = newPassword
    }

    fun updateInfo(name: String, phone: String?, profileImage: String?) {
        this.name = name
        this.phone = phone
        this.profileImage = profileImage
    }
}