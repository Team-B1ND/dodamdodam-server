package com.b1nd.dodamdodam.notice.domain.notice.repository

import com.b1nd.dodamdodam.notice.domain.notice.entity.NoticeEntity
import com.b1nd.dodamdodam.notice.domain.notice.entity.NoticeFileEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface NoticeFileRepository : JpaRepository<NoticeFileEntity, Long> {

    @EntityGraph(attributePaths = ["notice"])
    fun findAllByNoticeIn(notices: List<NoticeEntity>): List<NoticeFileEntity>

    fun deleteAllByNotice(notice: NoticeEntity)
}
