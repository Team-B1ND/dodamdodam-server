package com.b1nd.dodamdodam.club.domain.club.repository

import com.b1nd.dodamdodam.club.domain.club.entity.ClubEntity
import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubStatus
import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubType
import org.springframework.data.jpa.repository.JpaRepository

interface ClubRepository : JpaRepository<ClubEntity, Long> {
    fun findAllByStateNot(state: ClubStatus): List<ClubEntity>
    fun findByIdAndStateNot(id: Long, state: ClubStatus): ClubEntity?
    fun existsByName(name: String): Boolean
    fun findByIdIn(ids: List<Long>): List<ClubEntity>
    fun findByTypeAndState(type: ClubType, state: ClubStatus): List<ClubEntity>
}
