package com.b1nd.dodamdodam.inapp.domain.app.repository

import com.b1nd.dodamdodam.inapp.domain.app.entity.AppReleaseEntity
import com.b1nd.dodamdodam.inapp.domain.app.entity.AppEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface AppReleaseRepository : JpaRepository<AppReleaseEntity, Long> {
    fun findByPublicId(publicId: UUID): AppReleaseEntity?
    fun findAllByAppOrderByCreatedAtDesc(app: AppEntity): List<AppReleaseEntity>
    fun findAllByAppAndEnabledIsTrue(app: AppEntity): List<AppReleaseEntity>
}
