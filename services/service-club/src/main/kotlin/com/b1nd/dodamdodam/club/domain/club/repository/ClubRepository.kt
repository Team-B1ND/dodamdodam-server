package com.b1nd.dodamdodam.club.domain.club.repository

import com.b1nd.dodamdodam.club.domain.club.entity.ClubEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ClubRepository: JpaRepository<ClubEntity, Long> {
    fun findByPublicId(publicId: UUID): ClubEntity?
    fun existsByName(name: String): Boolean
}