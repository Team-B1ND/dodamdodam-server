package com.b1nd.dodamdodam.wakeupsong.application.wakeupsong.data.request

import jakarta.validation.constraints.NotBlank

data class WakeupSongUrlRequest(
    @field:NotBlank
    val videoUrl: String
)
