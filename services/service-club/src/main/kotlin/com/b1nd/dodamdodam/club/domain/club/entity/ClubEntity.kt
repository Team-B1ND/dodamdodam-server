package com.b1nd.dodamdodam.club.domain.club.entity

import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubStatus
import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubType
import com.b1nd.dodamdodam.core.jpa.entity.BaseTimeEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "club")
class ClubEntity(
    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var shortDescription: String,

    @Column(columnDefinition = "TEXT")
    var description: String? = null,

    @Column(nullable = false, columnDefinition = "TEXT")
    var image: String,

    @Column(nullable = false)
    var subject: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var type: ClubType,

    @Column(name = "teacher_id")
    var teacherId: Long? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var state: ClubStatus,

    var reason: String? = null,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun updateStatus(clubStatus: ClubStatus, reason: String?) {
        this.state = clubStatus
        this.reason = reason
    }

    fun updateStatus(name: String, clubStatus: ClubStatus) {
        this.name = name
        this.state = clubStatus
    }

    fun assignTeacher(teacherId: Long) {
        this.teacherId = teacherId
    }

    fun updateInfo(name: String, subject: String, shortDescription: String, description: String?, image: String) {
        this.name = name
        this.subject = subject
        this.shortDescription = shortDescription
        this.description = description
        this.image = image
    }
}
