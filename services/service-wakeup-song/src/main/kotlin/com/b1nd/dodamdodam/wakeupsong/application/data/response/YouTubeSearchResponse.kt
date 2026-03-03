package com.b1nd.dodamdodam.wakeupsong.application.data.response

data class YouTubeSearchResponse(
    val videoId: String,
    val videoTitle: String,
    val channelTitle: String,
    val thumbnail: String?,
    val videoUrl: String
)
