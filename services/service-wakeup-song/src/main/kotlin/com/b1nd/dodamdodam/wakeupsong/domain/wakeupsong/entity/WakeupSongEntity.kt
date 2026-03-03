package com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.entity

import com.b1nd.dodamdodam.core.jpa.entity.BaseTimeEntity
import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.enumeration.WakeupSongStatus
import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "wakeup_songs")
class WakeupSongEntity(

    @Column(nullable = false, columnDefinition = "BINARY(16)")
    val userId: UUID,

    @Column(nullable = false)
    val videoTitle: String,

    @Column(nullable = false)
    val videoId: String,

    @Column(nullable = false, length = 512)
    val videoUrl: String,

    @Column(nullable = false)
    val channelTitle: String,

    @Column(columnDefinition = "TEXT")
    val thumbnail: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: WakeupSongStatus = WakeupSongStatus.PENDING
) : BaseTimeEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun allow() {
        this.status = WakeupSongStatus.ALLOWED
    }

    fun deny() {
        this.status = WakeupSongStatus.DENIED
    }
}
