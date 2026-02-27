package com.b1nd.dodamdodam.wakeupsong.application.wakeupsong.data.response

import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.entity.WakeupSongEntity
import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.enumeration.WakeupSongStatusType

data class WakeupSongResponse(
    val thumbnail: String,
    val videoTitle: String,
    val videoId: String,
    val videoUrl: String,
    val channelTitle: String,
    val status: WakeupSongStatusType
) {
    companion object {
        fun fromEntity(entity: WakeupSongEntity): WakeupSongResponse =
            WakeupSongResponse(
                thumbnail = entity.thumbnailUrl,
                videoTitle = entity.videoTitle,
                videoId = entity.videoId,
                videoUrl = entity.videoUrl,
                channelTitle = entity.channelTitle,
                status = entity.status
            )
    }
}
