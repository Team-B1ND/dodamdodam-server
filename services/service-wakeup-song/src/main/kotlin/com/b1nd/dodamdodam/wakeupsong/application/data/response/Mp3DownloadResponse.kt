package com.b1nd.dodamdodam.wakeupsong.application.data.response

import java.nio.file.Path

data class Mp3DownloadResponse(
    val file: Path,
    val title: String
)
