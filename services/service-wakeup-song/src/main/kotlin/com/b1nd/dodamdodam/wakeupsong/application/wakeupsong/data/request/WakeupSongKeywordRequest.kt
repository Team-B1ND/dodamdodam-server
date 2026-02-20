package com.b1nd.dodamdodam.wakeupsong.application.wakeupsong.data.request

import jakarta.validation.constraints.NotBlank

data class WakeupSongKeywordRequest(
    @field:NotBlank
    val artist: String,
    @field:NotBlank
    val title: String
)
