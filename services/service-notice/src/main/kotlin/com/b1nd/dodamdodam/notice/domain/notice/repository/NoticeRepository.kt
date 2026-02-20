package com.b1nd.dodamdodam.notice.domain.notice.repository

import com.b1nd.dodamdodam.notice.domain.notice.entity.NoticeEntity
import org.springframework.data.jpa.repository.JpaRepository

interface NoticeRepository : JpaRepository<NoticeEntity, Long>, NoticeRepositoryCustom
