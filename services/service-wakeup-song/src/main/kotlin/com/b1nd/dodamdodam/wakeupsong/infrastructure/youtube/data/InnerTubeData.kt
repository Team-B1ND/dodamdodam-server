package com.b1nd.dodamdodam.wakeupsong.infrastructure.youtube.data

data class InnerTubeSearchRequest(
    val context: InnerTubeContext,
    val query: String
)

data class InnerTubeContext(
    val client: InnerTubeClient
)

data class InnerTubeClient(
    val clientName: String = "WEB",
    val clientVersion: String = "2.20231219.04.00",
    val hl: String = "ko",
    val gl: String = "KR"
)
