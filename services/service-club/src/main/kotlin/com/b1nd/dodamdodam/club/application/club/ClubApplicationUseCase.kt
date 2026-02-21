package com.b1nd.dodamdodam.club.application.club

import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubPriority
import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubStatus
import com.b1nd.dodamdodam.club.domain.club.service.ClubMemberService
import com.b1nd.dodamdodam.club.domain.club.service.ClubService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(rollbackFor = [Exception::class])
class ClubApplicationUseCase(
    private val clubService: ClubService,
    private val clubMemberService: ClubMemberService,
) {
    companion object {
        private const val MAX_STUDENT_COUNT = 22
    }

    fun assignmentClubMembers() {
        val clubs = clubService.getCreativeActivityClubs()
        val allMembers = clubMemberService.getPendingAndAllowedMembersByClubs(clubs)

        val clubMemberMap = allMembers.groupBy { it.club }

        val selectedMembers = mutableListOf<com.b1nd.dodamdodam.club.domain.club.entity.ClubMemberEntity>()

        ClubPriority.getClubPriorities().forEach { priority ->
            val members = clubMemberService.shuffleClubMemberMap(
                maxCount = MAX_STUDENT_COUNT,
                clubMemberMap = clubMemberMap,
                priority = priority,
            )
            selectedMembers.addAll(members)
        }

        clubMemberService.updateStatus(selectedMembers, ClubStatus.ALLOWED)
    }
}
