package com.b1nd.dodamdodam.club.presentation.club

import com.b1nd.dodamdodam.club.application.club.ClubUseCase
import com.b1nd.dodamdodam.club.application.club.data.request.CreateClubRequest
import com.b1nd.dodamdodam.club.application.club.data.request.TransferOwnerRequest
import com.b1nd.dodamdodam.club.application.club.data.request.UpdateClubRequest
import com.b1nd.dodamdodam.core.security.annotation.authentication.UserAccess
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/clubs")
class ClubController(
    private val clubUseCase: ClubUseCase
) {
    @UserAccess
    @PostMapping
    fun createClub(@RequestBody request: CreateClubRequest) =
        clubUseCase.createClub(request)

    @UserAccess
    @GetMapping("/{publicId}")
    fun getClub(@PathVariable publicId: UUID) =
        clubUseCase.getClub(publicId)

    @UserAccess
    @GetMapping
    fun getClubs() =
        clubUseCase.getClubs()

    @UserAccess
    @PatchMapping("/{publicId}")
    fun updateClub(@PathVariable publicId: UUID, @RequestBody request: UpdateClubRequest) =
        clubUseCase.updateClub(publicId, request)

    @UserAccess
    @DeleteMapping("/{publicId}")
    fun deleteClub(@PathVariable publicId: UUID) =
        clubUseCase.deleteClub(publicId)

    @UserAccess
    @PatchMapping("/{publicId}/owner")
    fun transferOwner(@PathVariable publicId: UUID, @RequestBody request: TransferOwnerRequest) =
        clubUseCase.transferOwner(publicId, request)
}
