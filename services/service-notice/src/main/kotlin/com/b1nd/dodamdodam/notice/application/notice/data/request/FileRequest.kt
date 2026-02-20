package com.b1nd.dodamdodam.notice.application.notice.data.request

import com.b1nd.dodamdodam.notice.domain.notice.enumeration.FileType

data class FileRequest(
    val url: String,
    val name: String,
    val fileType: FileType
)
