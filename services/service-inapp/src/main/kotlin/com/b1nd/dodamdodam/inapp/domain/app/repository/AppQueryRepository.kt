package com.b1nd.dodamdodam.inapp.domain.app.repository

import com.b1nd.dodamdodam.inapp.domain.app.entity.AppEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

data class ActiveAppWithRelease(
    val app: AppEntity,
    val releasePublicId: UUID?,
)

interface AppQueryRepository {
    fun findActiveApps(pageable: Pageable): Page<AppEntity>
    fun findActiveAppsWithRelease(pageable: Pageable): Page<ActiveAppWithRelease>
}
