package com.b1nd.dodamdodam.wakeupsong.presentation

import com.b1nd.dodamdodam.core.security.annotation.authentication.UserAccess
import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
import com.b1nd.dodamdodam.wakeupsong.application.WakeupSongUseCase
import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class WakeupSongDownloadController(
    private val wakeupSongUseCase: WakeupSongUseCase
) {

    @UserAccess(roles = [RoleType.BROADCASTER])
    @GetMapping("/download")
    fun downloadMp3(@RequestParam url: String): ResponseEntity<Resource> =
        wakeupSongUseCase.downloadMp3(url)
}
