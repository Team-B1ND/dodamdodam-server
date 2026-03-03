package com.b1nd.dodamdodam.wakeupsong.presentation

import com.b1nd.dodamdodam.core.security.annotation.authentication.UserAccess
import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
import com.b1nd.dodamdodam.wakeupsong.application.WakeupSongUseCase
import com.b1nd.dodamdodam.wakeupsong.application.data.request.ApplyWakeupSongByKeywordRequest
import com.b1nd.dodamdodam.wakeupsong.application.data.request.ApplyWakeupSongRequest
import org.springframework.web.bind.annotation.*

@RestController
class WakeupSongController(
    private val wakeupSongUseCase: WakeupSongUseCase
) {

    @UserAccess(hasAnyRoleOnly = true)
    @GetMapping("/my")
    fun getMyWakeupSongs() = wakeupSongUseCase.getMyWakeupSongs()

    @UserAccess(roles = [RoleType.STUDENT])
    @GetMapping("/allowed")
    fun getAllowed(
        @RequestParam year: Int,
        @RequestParam month: Int,
        @RequestParam day: Int
    ) = wakeupSongUseCase.getAllowed(year, month, day)

    @UserAccess(roles = [RoleType.TEACHER])
    @GetMapping("/pending")
    fun getPending() = wakeupSongUseCase.getPending()

    @UserAccess(hasAnyRoleOnly = true)
    @GetMapping("/chart")
    fun getMelonChart() = wakeupSongUseCase.getMelonChart()

    @UserAccess(hasAnyRoleOnly = true)
    @GetMapping("/search")
    fun searchYouTube(@RequestParam keyword: String) = wakeupSongUseCase.searchYouTube(keyword)

    @UserAccess(roles = [RoleType.STUDENT])
    @PostMapping
    fun applyWakeupSong(@RequestBody request: ApplyWakeupSongRequest) =
        wakeupSongUseCase.applyWakeupSong(request)

    @UserAccess(roles = [RoleType.STUDENT])
    @PostMapping("/keyword")
    fun applyByKeyword(@RequestBody request: ApplyWakeupSongByKeywordRequest) =
        wakeupSongUseCase.applyByKeyword(request)

    @UserAccess(roles = [RoleType.TEACHER])
    @PatchMapping("/allow/{id}")
    fun allow(@PathVariable id: Long) = wakeupSongUseCase.allow(id)

    @UserAccess(roles = [RoleType.TEACHER])
    @PatchMapping("/deny/{id}")
    fun deny(@PathVariable id: Long) = wakeupSongUseCase.deny(id)

    @UserAccess(hasAnyRoleOnly = true)
    @DeleteMapping("/my/{id}")
    fun deleteMyWakeupSong(@PathVariable id: Long) = wakeupSongUseCase.deleteMyWakeupSong(id)
}
