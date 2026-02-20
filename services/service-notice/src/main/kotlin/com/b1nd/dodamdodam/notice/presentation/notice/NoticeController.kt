package com.b1nd.dodamdodam.notice.presentation.notice

import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.notice.application.notice.NoticeUseCase
import com.b1nd.dodamdodam.notice.application.notice.data.request.GenerateNoticeRequest
import com.b1nd.dodamdodam.notice.application.notice.data.request.ModifyNoticeRequest
import com.b1nd.dodamdodam.notice.application.notice.data.response.NoticeResponse
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/notice")
class NoticeController(
    private val noticeUseCase: NoticeUseCase
) {
    @PostMapping
    fun generate(@RequestBody request: GenerateNoticeRequest): Response<Long> {
        return noticeUseCase.register(request)
    }

    @GetMapping
    fun getNotices(
        @RequestParam(required = false) keyword: String?,
        @RequestParam(required = false) lastId: Long?,
        @RequestParam(defaultValue = "10") limit: Int
    ): Response<List<NoticeResponse>> {
        return noticeUseCase.getNotices(keyword, lastId, limit)
    }

    @PatchMapping("/{id}")
    fun modify(
        @PathVariable id: Long,
        @RequestBody request: ModifyNoticeRequest
    ): Response<Unit> {
        return noticeUseCase.modify(id, request)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): Response<Unit> {
        return noticeUseCase.delete(id)
    }

    @GetMapping("/health")
    fun health(): String = "OK"
}
