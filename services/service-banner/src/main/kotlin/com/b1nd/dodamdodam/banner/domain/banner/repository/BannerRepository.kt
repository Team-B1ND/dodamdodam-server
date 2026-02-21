package com.b1nd.dodamdodam.banner.domain.banner.repository

import com.b1nd.dodamdodam.banner.domain.banner.entity.BannerEntity
import com.b1nd.dodamdodam.banner.domain.banner.enumeration.BannerStatus
import org.springframework.data.jpa.repository.JpaRepository

interface BannerRepository : JpaRepository<BannerEntity, Long> {
    fun findAllByOrderByIdDesc(): List<BannerEntity>
    fun findByStatusOrderByIdDesc(status: BannerStatus): List<BannerEntity>
}
