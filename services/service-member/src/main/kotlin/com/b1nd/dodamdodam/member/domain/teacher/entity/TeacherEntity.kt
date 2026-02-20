package com.b1nd.dodamdodam.member.domain.teacher.entity

import com.b1nd.dodamdodam.core.jpa.entity.BaseTimeEntity
import com.b1nd.dodamdodam.member.domain.member.entity.MemberEntity
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "teachers")
class TeacherEntity(
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_member_id", nullable = false)
    val member: MemberEntity,

    var tel: String,
    var position: String,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun updateInfo(tel: String?, position: String?) {
        tel?.takeIf { it.isNotBlank() }?.let { this.tel = it }
        position?.takeIf { it.isNotBlank() }?.let { this.position = it }
    }
}
