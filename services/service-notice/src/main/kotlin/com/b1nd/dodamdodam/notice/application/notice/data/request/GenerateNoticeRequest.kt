package com.b1nd.dodamdodam.notice.application.notice.data.request

import com.b1nd.dodamdodam.notice.domain.notice.entity.NoticeEntity
import com.b1nd.dodamdodam.notice.domain.notice.entity.NoticeFileEntity

data class GenerateNoticeRequest(
    val title: String,
    val content: String,
    val files: List<FileRequest>?
) {
    fun toEntity(userId: String): NoticeEntity {
        return NoticeEntity(
            title = title,
            content = content,
            userId = userId
        )
    }

    fun toNoticeFiles(notice: NoticeEntity): List<NoticeFileEntity> {
        if (files.isNullOrEmpty()) {
            return emptyList()
        }
        return files.map { file ->
            NoticeFileEntity(
                fileUrl = file.url,
                fileName = file.name,
                fileType = file.fileType,
                notice = notice
            )
        }
    }
}
