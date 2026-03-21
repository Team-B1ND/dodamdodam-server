package com.b1nd.dodamdodam.file.application.data.response

data class FileUploadResponse(
    val url: String,
    val originalFilename: String,
    val contentType: String?,
    val size: Long,
)
