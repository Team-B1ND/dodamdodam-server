package com.b1nd.dodamdodam.banner.application.banner

import com.b1nd.dodamdodam.banner.application.banner.data.request.BannerRequest
import com.b1nd.dodamdodam.banner.application.banner.data.response.BannerResponse
import com.b1nd.dodamdodam.banner.application.banner.data.toBannerEntity
import com.b1nd.dodamdodam.banner.domain.banner.service.BannerService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(rollbackFor = [Exception::class])
class BannerUseCase(
    private val bannerService: BannerService,
) {
    fun create(request: BannerRequest) {
        bannerService.create(request.toBannerEntity())
    }

    fun modify(id: Long, request: BannerRequest) {
        val banner = bannerService.findById(id)
        banner.updateBanner(
            title = request.title,
            imageUrl = request.image,
            redirectUrl = request.url,
            expireAt = request.expireAt,
        )
    }

    fun activate(id: Long) {
        val banner = bannerService.findById(id)
        banner.activateStatus()
    }

    fun deactivate(id: Long) {
        val banner = bannerService.findById(id)
        banner.deactivateStatus()
    }

    fun deleteById(id: Long) {
        bannerService.deleteById(id)
    }

    @Transactional(readOnly = true)
    fun getById(id: Long): BannerResponse {
        val banner = bannerService.findById(id)
        return BannerResponse.fromEntity(banner)
    }

    @Transactional(readOnly = true)
    fun getActiveBanners(): List<BannerResponse> {
        return bannerService.findActiveBanners().map { BannerResponse.fromEntity(it) }
    }

    @Transactional(readOnly = true)
    fun getAllBanners(): List<BannerResponse> {
        return bannerService.findAll().map { BannerResponse.fromEntity(it) }
    }
}
