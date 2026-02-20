package com.b1nd.dodamdodam.notice.application.notice.data.response

import com.b1nd.dodamdodam.notice.domain.notice.entity.NoticeFileEntity
import com.b1nd.dodamdodam.notice.domain.notice.enumeration.FileType

data class NoticeFileResponse(
    val fileUrl: String,
    val fileName: String,
    val fileType: FileType
) {
    companion object {
        fun fromNoticeFileEntity(file: NoticeFileEntity): NoticeFileResponse {
            return NoticeFileResponse(
                fileUrl = file.fileUrl,
                fileName = file.fileName,
                fileType = file.fileType
            )
        }

        fun fromNoticeFileEntities(files: List<NoticeFileEntity>): List<NoticeFileResponse> {
            return files.map { fromNoticeFileEntity(it) }
        }
    }
}
