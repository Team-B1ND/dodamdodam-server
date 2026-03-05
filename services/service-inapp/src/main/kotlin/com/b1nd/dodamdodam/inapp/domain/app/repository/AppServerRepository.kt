package com.b1nd.dodamdodam.inapp.domain.app.repository

import com.b1nd.dodamdodam.inapp.domain.app.entity.AppEntity
import com.b1nd.dodamdodam.inapp.domain.app.entity.AppServerEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AppServerRepository : JpaRepository<AppServerEntity, Long> {
    fun existsByApp(app: AppEntity): Boolean
    fun findByApp(app: AppEntity): AppServerEntity?
}
