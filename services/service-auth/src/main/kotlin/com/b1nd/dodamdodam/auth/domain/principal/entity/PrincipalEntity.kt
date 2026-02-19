package com.b1nd.dodamdodam.auth.domain.principal.entity

import com.b1nd.dodamdodam.auth.domain.principal.converter.RoleTypeSetConverter
import com.b1nd.dodamdodam.core.jpa.entity.BaseTimeEntity
import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "principals")
class PrincipalEntity(
    @Column(name = "fk_user_id", unique = true)
    val userId: UUID,
    var status: Boolean,
    var username: String,
    @Convert(converter = RoleTypeSetConverter::class)
    var roles: Set<RoleType>
    ): BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}