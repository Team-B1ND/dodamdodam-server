package com.b1nd.dodamdodam.member.domain.student.entity

import com.b1nd.dodamdodam.core.jpa.entity.BaseTimeEntity
import com.b1nd.dodamdodam.member.domain.member.entity.MemberEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "students")
class StudentEntity(
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_member_id", nullable = false)
    val member: MemberEntity,

    var grade: Int,
    var room: Int,
    var number: Int,

    @Column(unique = true)
    val code: String? = null,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun updateInfo(grade: Int, room: Int, number: Int) {
        this.grade = grade
        this.room = room
        this.number = number
    }
}
