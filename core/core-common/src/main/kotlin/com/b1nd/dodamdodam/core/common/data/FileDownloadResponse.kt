package com.b1nd.dodamdodam.core.common.data

import org.springframework.http.MediaType

data class FileDownloadResponse(
    val bytes: ByteArray,
    val filename: String,
    val contentType: MediaType
)
