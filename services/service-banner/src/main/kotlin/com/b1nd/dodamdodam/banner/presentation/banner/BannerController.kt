package com.b1nd.dodamdodam.banner.presentation.banner

import com.b1nd.dodamdodam.banner.application.banner.BannerUseCase
import com.b1nd.dodamdodam.banner.application.banner.data.request.BannerRequest
import com.b1nd.dodamdodam.banner.application.banner.data.response.BannerResponse
import com.b1nd.dodamdodam.core.common.data.Response
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/banner")
class BannerController(
    private val bannerUseCase: BannerUseCase,
) {
    @PostMapping
    fun create(@RequestBody request: BannerRequest): Response<Unit> {
        bannerUseCase.create(request)
        return Response.created("배너 생성 성공")
    }

    @PatchMapping("/{id}")
    fun modify(
        @PathVariable id: Long,
        @RequestBody request: BannerRequest,
    ): Response<Unit> {
        bannerUseCase.modify(id, request)
        return Response.ok("배너 수정 성공")
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long): Response<Unit> {
        bannerUseCase.deleteById(id)
        return Response.ok("배너 삭제 성공")
    }

    @PatchMapping("/activate/{id}")
    fun activate(@PathVariable id: Long): Response<Unit> {
        bannerUseCase.activate(id)
        return Response.ok("배너 활성화 성공")
    }

    @PatchMapping("/deactivate/{id}")
    fun deactivate(@PathVariable id: Long): Response<Unit> {
        bannerUseCase.deactivate(id)
        return Response.ok("배너 비활성화 성공")
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): Response<BannerResponse> =
        Response.ok("배너 단일 조회 성공", bannerUseCase.getById(id))

    @GetMapping("/active")
    fun getActiveBanners(): Response<List<BannerResponse>> =
        Response.ok("활성화 배너 조회 성공", bannerUseCase.getActiveBanners())

    @GetMapping
    fun getAllBanners(): Response<List<BannerResponse>> =
        Response.ok("배너 전체 조회 성공", bannerUseCase.getAllBanners())

    @GetMapping("/health")
    fun health(): String = "OK"
}
