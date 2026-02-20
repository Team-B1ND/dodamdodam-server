package com.b1nd.dodamdodam.notice.domain.notice.service

import com.b1nd.dodamdodam.notice.domain.notice.entity.NoticeEntity
import com.b1nd.dodamdodam.notice.domain.notice.entity.NoticeFileEntity
import com.b1nd.dodamdodam.notice.domain.notice.enumeration.NoticeStatus
import com.b1nd.dodamdodam.notice.domain.notice.exception.NotNoticeAuthorException
import com.b1nd.dodamdodam.notice.domain.notice.exception.NoticeNotFoundException
import com.b1nd.dodamdodam.notice.domain.notice.repository.NoticeFileRepository
import com.b1nd.dodamdodam.notice.domain.notice.repository.NoticeRepository
import org.springframework.stereotype.Service

@Service
class NoticeService(
    private val noticeRepository: NoticeRepository,
    private val noticeFileRepository: NoticeFileRepository
) {
    fun create(notice: NoticeEntity): NoticeEntity {
        return noticeRepository.save(notice)
    }

    fun createFiles(files: List<NoticeFileEntity>) {
        noticeFileRepository.saveAll(files)
    }

    fun getById(id: Long): NoticeEntity {
        return noticeRepository.findById(id)
            .orElseThrow { NoticeNotFoundException() }
    }

    fun getAll(keyword: String?, lastId: Long?, limit: Int): List<NoticeEntity> {
        return noticeRepository.findAllByStatusAndKeyword(
            NoticeStatus.CREATED,
            keyword,
            lastId,
            limit
        )
    }

    fun getNoticeFileMap(notices: List<NoticeEntity>): Map<Long, List<NoticeFileEntity>> {
        if (notices.isEmpty()) {
            return emptyMap()
        }
        return noticeFileRepository.findAllByNoticeIn(notices)
            .groupBy { it.notice.id!! }
    }

    fun updateNotice(id: Long, userId: String, title: String?, content: String?) {
        val notice = getById(id)
        checkPermission(notice, userId)
        notice.updateNotice(title, content)
        noticeRepository.save(notice)
    }

    fun deleteNotice(id: Long, userId: String) {
        val notice = getById(id)
        checkPermission(notice, userId)
        notice.noticeStatus = NoticeStatus.DELETED
        noticeRepository.save(notice)
    }

    private fun checkPermission(notice: NoticeEntity, userId: String) {
        if (notice.userId != userId) {
            throw NotNoticeAuthorException()
        }
    }
}
