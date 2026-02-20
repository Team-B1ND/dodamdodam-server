package com.b1nd.dodamdodam.notice.domain.notice.entity

import com.b1nd.dodamdodam.notice.domain.notice.enumeration.FileType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "notice_files")
class NoticeFileEntity(
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    val fileUrl: String,

    @Column(nullable = false)
    val fileName: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val fileType: FileType,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_notice_id", nullable = false)
    val notice: NoticeEntity
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
