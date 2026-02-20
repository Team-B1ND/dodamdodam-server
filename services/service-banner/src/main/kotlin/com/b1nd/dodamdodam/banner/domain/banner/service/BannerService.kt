package com.b1nd.dodamdodam.banner.domain.banner.service

import com.b1nd.dodamdodam.banner.domain.banner.entity.BannerEntity
import com.b1nd.dodamdodam.banner.domain.banner.enumeration.BannerStatus
import com.b1nd.dodamdodam.banner.domain.banner.exception.BannerNotFoundException
import com.b1nd.dodamdodam.banner.domain.banner.repository.BannerRepository
import org.springframework.stereotype.Service

@Service
class BannerService(
    private val bannerRepository: BannerRepository,
) {
    fun create(banner: BannerEntity): BannerEntity = bannerRepository.save(banner)

    fun findById(id: Long): BannerEntity =
        bannerRepository.findById(id)
            .orElseThrow { BannerNotFoundException() }

    fun findAll(): List<BannerEntity> = bannerRepository.findAllByOrderByIdDesc()

    fun findActiveBanners(): List<BannerEntity> =
        bannerRepository.findByStatusOrderByIdDesc(BannerStatus.ACTIVE)

    fun deleteById(id: Long) {
        bannerRepository.deleteById(id)
    }
}
