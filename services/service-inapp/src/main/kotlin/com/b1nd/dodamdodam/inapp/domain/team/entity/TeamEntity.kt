package com.b1nd.dodamdodam.inapp.domain.team.entity

import com.b1nd.dodamdodam.core.jpa.entity.BaseTimeEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.PrePersist
import jakarta.persistence.Table
import org.hibernate.annotations.SoftDelete
import java.util.UUID

@Entity
@Table(name = "teams")
@SoftDelete
class TeamEntity(
    var name: String,
    var description: String? = null,
    @Column(columnDefinition = "TEXT")
    var iconUrl: String? = null,
    var githubUrl: String? = null,
): BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(nullable = false, unique = true)
    var publicId: UUID? = null
        protected set

    @PrePersist
    fun generatePublicId() {
        publicId = UUID.randomUUID()
    }

    fun update(name: String?, description: String?, iconUrl: String?, githubUrl: String?) {
        name?.let { this.name = name }
        description?.let { this.description = description }
        iconUrl?.let { this.iconUrl = iconUrl }
        githubUrl?.let { this.githubUrl = githubUrl }
    }
}