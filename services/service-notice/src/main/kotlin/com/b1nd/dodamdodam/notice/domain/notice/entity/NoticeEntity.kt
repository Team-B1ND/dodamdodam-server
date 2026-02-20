package com.b1nd.dodamdodam.notice.domain.notice.entity

import com.b1nd.dodamdodam.core.jpa.entity.BaseTimeEntity
import com.b1nd.dodamdodam.notice.domain.notice.enumeration.NoticeStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "notices")
class NoticeEntity(
    @Column(nullable = false)
    var title: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var noticeStatus: NoticeStatus = NoticeStatus.CREATED,

    @Column(nullable = false)
    val userId: String
): BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun updateNotice(title: String?, content: String?) {
        title?.let { this.title = it }
        content?.let { this.content = it }
    }
}
