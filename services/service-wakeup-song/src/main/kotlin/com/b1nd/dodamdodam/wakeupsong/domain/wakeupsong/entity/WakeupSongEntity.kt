package com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.entity

import com.b1nd.dodamdodam.core.jpa.entity.BaseTimeEntity
import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.enumeration.WakeupSongStatusType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "wakeup_songs")
class WakeupSongEntity(

    @Column(nullable = false, name = "fk_student_id")
    val studentId: UUID,

    @Column(nullable = false, length = 20)
    val videoId: String,

    @Column(nullable = false)
    val videoTitle: String,

    @Column(nullable = false, length = 500)
    val videoUrl: String,

    @Column(nullable = false)
    val channelTitle: String,

    @Column(nullable = false, length = 500)
    val thumbnailUrl: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: WakeupSongStatusType = WakeupSongStatusType.PENDING,

    var playAt: LocalDate? = null

) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
