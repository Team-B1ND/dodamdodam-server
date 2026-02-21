package com.b1nd.dodamdodam.club.domain.club.repository

import com.b1nd.dodamdodam.club.domain.club.entity.ClubTimeEntity
import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubTimeType
import org.springframework.data.jpa.repository.JpaRepository

interface ClubTimeRepository : JpaRepository<ClubTimeEntity, ClubTimeType>
