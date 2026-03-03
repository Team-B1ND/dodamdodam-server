package com.b1nd.dodamdodam.wakeupsong.application.data

import com.b1nd.dodamdodam.wakeupsong.application.data.response.WakeupSongResponse
import com.b1nd.dodamdodam.wakeupsong.application.data.response.YouTubeSearchResponse
import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.entity.WakeupSongEntity
import java.util.UUID

fun WakeupSongEntity.toResponse(): WakeupSongResponse =
    WakeupSongResponse(
        id = id!!,
        userId = userId,
        videoTitle = videoTitle,
        videoId = videoId,
        videoUrl = videoUrl,
        channelTitle = channelTitle,
        thumbnail = thumbnail,
        status = status,
        createdAt = createdAt,
        modifiedAt = modifiedAt
    )

fun YouTubeSearchResponse.toEntity(userId: UUID): WakeupSongEntity =
    WakeupSongEntity(
        userId = userId,
        videoTitle = videoTitle,
        videoId = videoId,
        videoUrl = videoUrl,
        channelTitle = channelTitle,
        thumbnail = thumbnail
    )
