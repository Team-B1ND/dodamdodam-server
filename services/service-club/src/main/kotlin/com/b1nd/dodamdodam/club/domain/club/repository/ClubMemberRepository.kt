package com.b1nd.dodamdodam.club.domain.club.repository

import com.b1nd.dodamdodam.club.domain.club.entity.ClubEntity
import com.b1nd.dodamdodam.club.domain.club.entity.ClubMemberEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ClubMemberRepository: JpaRepository<ClubMemberEntity, Long> {
    fun findByClubAndUserId(club: ClubEntity, userId: UUID): ClubMemberEntity?
    fun deleteAllByClub(club: ClubEntity)
}