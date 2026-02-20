package com.b1nd.dodamdodam.banner.domain.banner.exception

import com.b1nd.dodamdodam.core.common.exception.ExceptionCode
import org.springframework.http.HttpStatus

enum class BannerExceptionCode(
    override val status: HttpStatus,
    override val message: String,
) : ExceptionCode {
    BANNER_NOT_FOUND(HttpStatus.NOT_FOUND, "배너를 찾을 수 없습니다."),
}
