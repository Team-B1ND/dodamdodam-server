package com.b1nd.dodamdodam.outsleeping.domain.outsleeping.repository

import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.entity.QOutSleepingEntity
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.enumeration.OutSleepingStatusType
import com.querydsl.jpa.impl.JPAQueryFactory
import java.time.LocalDate
import java.util.UUID

class OutSleepingRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : OutSleepingRepositoryCustom {

    override fun existsOverlapping(studentId: UUID, startAt: LocalDate, endAt: LocalDate): Boolean {
        val qEntity = QOutSleepingEntity.outSleepingEntity
        return queryFactory
            .selectOne()
            .from(qEntity)
            .where(
                qEntity.studentId.eq(studentId),
                qEntity.status.`in`(OutSleepingStatusType.PENDING, OutSleepingStatusType.ALLOWED),
                qEntity.startAt.loe(endAt),
                qEntity.endAt.goe(startAt)
            )
            .fetchFirst() != null
    }
}
