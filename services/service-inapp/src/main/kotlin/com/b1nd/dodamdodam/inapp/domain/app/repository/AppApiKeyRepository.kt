package com.b1nd.dodamdodam.inapp.domain.app.repository

import com.b1nd.dodamdodam.inapp.domain.app.entity.AppApiKeyEntity
import com.b1nd.dodamdodam.inapp.domain.app.entity.AppEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AppApiKeyRepository : JpaRepository<AppApiKeyEntity, Long> {
    fun findByApp(app: AppEntity): AppApiKeyEntity?
    fun existsByApp(app: AppEntity): Boolean
}