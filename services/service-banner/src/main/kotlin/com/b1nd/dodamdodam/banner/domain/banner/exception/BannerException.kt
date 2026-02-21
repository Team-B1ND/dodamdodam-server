package com.b1nd.dodamdodam.banner.domain.banner.exception

import com.b1nd.dodamdodam.core.common.exception.BasicException

class BannerNotFoundException : BasicException(BannerExceptionCode.BANNER_NOT_FOUND)
