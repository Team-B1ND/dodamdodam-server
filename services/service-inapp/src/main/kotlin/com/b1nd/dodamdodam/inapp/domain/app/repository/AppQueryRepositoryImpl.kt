package com.b1nd.dodamdodam.inapp.domain.app.repository

import com.b1nd.dodamdodam.inapp.domain.app.entity.AppEntity
import com.b1nd.dodamdodam.inapp.domain.app.entity.QAppEntity.appEntity
import com.b1nd.dodamdodam.inapp.domain.app.enumeration.AppStatusType
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class AppQueryRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : AppQueryRepository {

    override fun findActiveApps(pageable: Pageable): Page<AppEntity> {
        val content = queryFactory
            .selectFrom(appEntity)
            .where(
                appEntity.releaseEnabled.isTrue,
                appEntity.releaseStatus.eq(AppStatusType.ALLOWED),
            )
            .orderBy(appEntity.id.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val total = queryFactory
            .select(appEntity.count())
            .from(appEntity)
            .where(
                appEntity.releaseEnabled.isTrue,
                appEntity.releaseStatus.eq(AppStatusType.ALLOWED),
            )
            .fetchOne() ?: 0L

        return PageImpl(content, pageable, total)
    }
}