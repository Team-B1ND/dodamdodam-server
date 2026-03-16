package com.b1nd.dodamdodam.inapp.domain.app.repository

import com.b1nd.dodamdodam.inapp.domain.app.entity.AppEntity
import com.b1nd.dodamdodam.inapp.domain.app.entity.AppReleaseEntity
import com.b1nd.dodamdodam.inapp.domain.app.entity.QAppReleaseEntity.appReleaseEntity
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class AppReleaseQueryRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : AppReleaseQueryRepository {

    override fun findReleases(
        app: AppEntity,
        date: LocalDate?,
        keyword: String?,
        pageable: Pageable,
    ): Page<AppReleaseEntity> {
        val content = queryFactory
            .selectFrom(appReleaseEntity)
            .where(
                appEq(app),
                dateEq(date),
                keywordContains(keyword),
            )
            .orderBy(appReleaseEntity.createdAt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val total = queryFactory
            .select(appReleaseEntity.count())
            .from(appReleaseEntity)
            .where(
                appEq(app),
                dateEq(date),
                keywordContains(keyword),
            )
            .fetchOne() ?: 0L

        return PageImpl(content, pageable, total)
    }

    private fun appEq(app: AppEntity): BooleanExpression =
        appReleaseEntity.app.eq(app)

    private fun dateEq(date: LocalDate?): BooleanExpression? =
        date?.let {
            appReleaseEntity.createdAt.between(
                it.atStartOfDay(),
                it.plusDays(1).atStartOfDay()
            )
        }

    private fun keywordContains(keyword: String?): BooleanExpression? =
        keyword?.takeIf { it.isNotBlank() }?.let {
            appReleaseEntity.releaseUrl.containsIgnoreCase(it)
                .or(appReleaseEntity.memo.containsIgnoreCase(it))
        }
}