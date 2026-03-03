package com.b1nd.dodamdodam.wakeupsong.application.data.response

import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.enumeration.WakeupSongStatus
import java.time.LocalDateTime
import java.util.UUID

data class WakeupSongResponse(
    val id: Long,
    val userId: UUID,
    val videoTitle: String,
    val videoId: String,
    val videoUrl: String,
    val channelTitle: String,
    val thumbnail: String?,
    val status: WakeupSongStatus,
    val createdAt: LocalDateTime?,
    val modifiedAt: LocalDateTime?
)
