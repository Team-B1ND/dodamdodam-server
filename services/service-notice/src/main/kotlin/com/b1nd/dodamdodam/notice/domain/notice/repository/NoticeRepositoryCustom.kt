package com.b1nd.dodamdodam.notice.domain.notice.repository

import com.b1nd.dodamdodam.notice.domain.notice.entity.NoticeEntity
import com.b1nd.dodamdodam.notice.domain.notice.enumeration.NoticeStatus

interface NoticeRepositoryCustom {
    fun findAllByStatusAndKeyword(
        status: NoticeStatus,
        keyword: String?,
        lastId: Long?,
        limit: Int
    ): List<NoticeEntity>
}
