package com.b1nd.dodamdodam.inapp.domain.app.repository

import com.b1nd.dodamdodam.inapp.domain.app.entity.AppEntity
import com.b1nd.dodamdodam.inapp.domain.app.entity.AppReleaseEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDate

interface AppReleaseQueryRepository {
    fun findReleases(
        app: AppEntity,
        date: LocalDate?,
        keyword: String?,
        pageable: Pageable,
    ): Page<AppReleaseEntity>
}