package com.b1nd.dodamdodam.user.domain.admin.entity

import com.b1nd.dodamdodam.core.jpa.entity.BaseTimeEntity
import com.b1nd.dodamdodam.user.domain.user.entity.UserEntity
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "admins")
class AdminEntity(
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "fk_user_id",
        nullable = false
    )
    val user: UserEntity,
    var githubId: String,
): BaseTimeEntity() {
    @Id
    val id: Long? = null
}