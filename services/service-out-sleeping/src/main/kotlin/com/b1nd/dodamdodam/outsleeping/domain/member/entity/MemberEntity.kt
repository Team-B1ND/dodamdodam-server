package com.b1nd.dodamdodam.outsleeping.domain.member.entity

import com.b1nd.dodamdodam.core.jpa.entity.BaseTimeEntity
import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "members")
class MemberEntity(

    @Column(nullable = false, unique = true, columnDefinition = "BINARY(16)")
    val userId: UUID,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false, length = 20)
    var role: String,

    var grade: Int? = null,

    var room: Int? = null,

    var number: Int? = null,
) : BaseTimeEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun isStudent() = role == "STUDENT"

    fun update(name: String, role: String, grade: Int?, room: Int?, number: Int?) {
        this.name = name
        this.role = role
        this.grade = grade
        this.room = room
        this.number = number
    }
}
