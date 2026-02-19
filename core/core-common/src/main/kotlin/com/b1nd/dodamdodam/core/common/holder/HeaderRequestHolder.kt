package com.b1nd.dodamdodam.core.common.holder

import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

object HeaderRequestHolder {
    fun current(): HttpServletRequest =
        (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
}