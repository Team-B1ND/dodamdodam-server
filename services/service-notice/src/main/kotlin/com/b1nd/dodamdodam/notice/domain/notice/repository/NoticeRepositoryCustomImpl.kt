package com.b1nd.dodamdodam.notice.domain.notice.repository

import com.b1nd.dodamdodam.notice.domain.notice.entity.NoticeEntity
import com.b1nd.dodamdodam.notice.domain.notice.entity.QNoticeEntity.noticeEntity
import com.b1nd.dodamdodam.notice.domain.notice.enumeration.NoticeStatus
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class NoticeRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : NoticeRepositoryCustom {

    override fun findAllByStatusAndKeyword(
        status: NoticeStatus,
        keyword: String?,
        lastId: Long?,
        limit: Int
    ): List<NoticeEntity> {
        return queryFactory
            .selectFrom(noticeEntity)
            .where(
                noticeEntity.noticeStatus.eq(status),
                keywordContains(keyword),
                lastIdLt(lastId)
            )
            .orderBy(noticeEntity.id.desc())
            .limit(limit.toLong())
            .fetch()
    }

    private fun keywordContains(keyword: String?): BooleanExpression? {
        return keyword?.let { noticeEntity.title.contains(it) }
    }

    private fun lastIdLt(lastId: Long?): BooleanExpression? {
        return lastId?.let { noticeEntity.id.lt(it) }
    }
}
