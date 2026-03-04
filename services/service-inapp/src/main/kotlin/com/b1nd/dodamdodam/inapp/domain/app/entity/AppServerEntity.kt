package com.b1nd.dodamdodam.inapp.domain.app.entity

import com.b1nd.dodamdodam.core.jpa.entity.BaseTimeEntity
import com.b1nd.dodamdodam.inapp.domain.app.enumeration.AppStatusType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "app_servers")
class AppServerEntity(
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_app_id", nullable = false, unique = true)
    val app: AppEntity,

    var name: String,
    @Column(nullable = false)
    var serverAddress: String,
    @Column(nullable = false)
    var redirectPath: String,
    @Column(nullable = false)
    var prefixLevel: Int,
    @Column(nullable = false)
    var enabled: Boolean,
    @Column(columnDefinition = "TEXT")
    var denyResult: String? = null,
    @Enumerated(EnumType.STRING)
    var status: AppStatusType,
): BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun updateServerInfo(
        name: String?,
        serverAddress: String?,
        redirectPath: String?,
        prefixLevel: Int?
    ) {
        var changed = false
        name?.let {
            if (this.name != it) {
                this.name = it
                changed = true
            }
        }
        serverAddress?.let {
            if (this.serverAddress != it) {
                this.serverAddress = it
                changed = true
            }
        }
        redirectPath?.let {
            if (this.redirectPath != it) {
                this.redirectPath = it
                changed = true
            }
        }
        prefixLevel?.let {
            if (this.prefixLevel != it) {
                this.prefixLevel = it
                changed = true
            }
        }

        // 서버 실제 배포 정보가 바뀌면 재승인이 필요하다.
        if (changed) {
            status = AppStatusType.PENDING
            enabled = false
            denyResult = null
        }
    }

    fun updateStatus(status: AppStatusType, denyResult: String?) {
        this.status = status
        this.denyResult = denyResult
        this.enabled = status == AppStatusType.ALLOWED
    }

    fun updateEnabled(enabled: Boolean) {
        this.enabled = enabled
    }
}
