package com.b1nd.dodamdodam.notice.domain.notice.repository

import com.b1nd.dodamdodam.notice.domain.notice.entity.NoticeEntity
import com.b1nd.dodamdodam.notice.domain.notice.enumeration.NoticeStatus
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface NoticeRepository : JpaRepository<NoticeEntity, Long> {

    @Query("""
        SELECT n FROM NoticeEntity n
        WHERE n.noticeStatus = :status
        AND (:keyword IS NULL OR n.title LIKE %:keyword%)
        AND (:lastId IS NULL OR n.id < :lastId)
        ORDER BY n.id DESC
    """)
    fun findAllByStatusAndKeyword(
        @Param("status") status: NoticeStatus,
        @Param("keyword") keyword: String?,
        @Param("lastId") lastId: Long?,
        pageable: Pageable
    ): List<NoticeEntity>
}
