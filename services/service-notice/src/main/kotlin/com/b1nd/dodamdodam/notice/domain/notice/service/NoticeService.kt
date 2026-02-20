package com.b1nd.dodamdodam.notice.domain.notice.service

import com.b1nd.dodamdodam.notice.domain.notice.entity.NoticeEntity
import com.b1nd.dodamdodam.notice.domain.notice.entity.NoticeFileEntity
import com.b1nd.dodamdodam.notice.domain.notice.enumeration.NoticeStatus
import com.b1nd.dodamdodam.notice.domain.notice.exception.NotNoticeAuthorException
import com.b1nd.dodamdodam.notice.domain.notice.exception.NoticeNotFoundException
import com.b1nd.dodamdodam.notice.domain.notice.repository.NoticeFileRepository
import com.b1nd.dodamdodam.notice.domain.notice.repository.NoticeRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class NoticeService(
    private val noticeRepository: NoticeRepository,
    private val noticeFileRepository: NoticeFileRepository
) {
    fun save(notice: NoticeEntity): NoticeEntity {
        return noticeRepository.save(notice)
    }

    fun saveAllFiles(files: List<NoticeFileEntity>) {
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
            PageRequest.of(0, limit)
        )
    }

    fun getNoticeFileMap(notices: List<NoticeEntity>): Map<Long, List<NoticeFileEntity>> {
        if (notices.isEmpty()) {
            return emptyMap()
        }
        return noticeFileRepository.findAllByNoticeIn(notices)
            .groupBy { it.notice.id!! }
    }

    fun updateNotice(id: Long, memberId: String, title: String?, content: String?) {
        val notice = getById(id)
        checkPermission(notice, memberId)
        notice.updateNotice(title, content)
        noticeRepository.save(notice)
    }

    fun deleteNotice(id: Long, memberId: String) {
        val notice = getById(id)
        checkPermission(notice, memberId)
        notice.noticeStatus = NoticeStatus.DELETED
        noticeRepository.save(notice)
    }

    private fun checkPermission(notice: NoticeEntity, memberId: String) {
        if (notice.memberId != memberId) {
            throw NotNoticeAuthorException()
        }
    }
}
