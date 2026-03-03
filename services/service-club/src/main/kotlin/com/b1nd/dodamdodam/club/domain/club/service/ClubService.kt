package com.b1nd.dodamdodam.club.domain.club.service

import com.b1nd.dodamdodam.club.domain.club.entity.ClubEntity
import com.b1nd.dodamdodam.club.domain.club.entity.ClubMemberEntity
import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubType
import com.b1nd.dodamdodam.club.domain.club.repository.ClubMemberRepository
import com.b1nd.dodamdodam.club.domain.club.repository.ClubRepository
import com.b1nd.dodamdodam.club.infrastructure.exception.ClubAlreadyExistsException
import com.b1nd.dodamdodam.club.infrastructure.exception.ClubNotFoundException
import com.b1nd.dodamdodam.club.infrastructure.exception.ClubNotOwnerException
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ClubService(
    private val clubRepository: ClubRepository,
    private val clubMemberRepository: ClubMemberRepository
) {
    fun get(publicId: UUID): ClubEntity =
        clubRepository.findByPublicId(publicId)
            ?: throw ClubNotFoundException()

    fun getAll(): List<ClubEntity> =
        clubRepository.findAll()

    fun create(userId: UUID, club: ClubEntity) {
        checkDuplicateClub(club.name)
        val savedClub = clubRepository.save(club)
        clubMemberRepository.save(ClubMemberEntity(savedClub, userId, true))
    }

    fun update(publicId: UUID, userId: UUID, name: String?, description: String?, imageUrl: String?, category: String?, type: ClubType?): ClubEntity {
        val club = get(publicId)
        validateOwner(club, userId)
        name?.let {
            if (it != club.name) checkDuplicateClub(it)
            club.name = it
        }
        description?.let { club.description = it }
        imageUrl?.let { club.imageUrl = it }
        category?.let { club.category = it }
        type?.let { club.type = it }
        return clubRepository.save(club)
    }

    fun delete(publicId: UUID, userId: UUID) {
        val club = get(publicId)
        validateOwner(club, userId)
        clubMemberRepository.deleteAllByClub(club)
        clubRepository.delete(club)
    }

    private fun validateOwner(club: ClubEntity, userId: UUID) {
        val member = clubMemberRepository.findByClubAndUserId(club, userId)
        if (member == null || !member.isOwner) throw ClubNotOwnerException()
    }

    private fun checkDuplicateClub(name: String) {
        if (clubRepository.existsByName(name))
            throw ClubAlreadyExistsException()
    }
}
