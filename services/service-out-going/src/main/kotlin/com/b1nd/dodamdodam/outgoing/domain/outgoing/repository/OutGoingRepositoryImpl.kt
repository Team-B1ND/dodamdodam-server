package com.b1nd.dodamdodam.outgoing.domain.outgoing.repository

import com.b1nd.dodamdodam.outgoing.domain.outgoing.entity.QOutGoingEntity
import com.b1nd.dodamdodam.outgoing.domain.outgoing.enumeration.OutGoingStatusType
import com.querydsl.jpa.impl.JPAQueryFactory
import java.time.LocalDateTime
import java.util.UUID

class OutGoingRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : OutGoingRepositoryCustom {

    override fun existsOverlapping(studentId: UUID, startAt: LocalDateTime, endAt: LocalDateTime): Boolean {
        val outGoing = QOutGoingEntity.outGoingEntity
        return queryFactory
            .selectOne()
            .from(outGoing)
            .where(
                outGoing.studentId.eq(studentId),
                outGoing.status.`in`(OutGoingStatusType.PENDING, OutGoingStatusType.ALLOWED),
                outGoing.startAt.loe(endAt),
                outGoing.endAt.goe(startAt)
            )
            .fetchFirst() != null
    }
}
