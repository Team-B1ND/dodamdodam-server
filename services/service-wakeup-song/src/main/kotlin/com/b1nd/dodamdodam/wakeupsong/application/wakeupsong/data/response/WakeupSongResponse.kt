package com.b1nd.dodamdodam.wakeupsong.application.wakeupsong.data.response

import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.entity.WakeupSongEntity
import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.enumeration.WakeupSongStatus
import java.time.LocalDateTime

data class WakeupSongResponse(
    val id: Long,
    val thumbnail: String,
    val videoTitle: String,
    val videoId: String,
    val videoUrl: String,
    val channelTitle: String,
    val status: WakeupSongStatus,
    val createdAt: LocalDateTime?
) {
    companion object {
        fun of(entity: WakeupSongEntity): WakeupSongResponse =
            WakeupSongResponse(
                id = entity.id!!,
                thumbnail = entity.thumbnailUrl,
                videoTitle = entity.videoTitle,
                videoId = entity.videoId,
                videoUrl = entity.videoUrl,
                channelTitle = entity.channelTitle,
                status = entity.status,
                createdAt = entity.createdAt
            )
    }
}
