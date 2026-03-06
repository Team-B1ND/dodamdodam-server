package com.b1nd.dodamdodam.nightstudy.domain.ban.service

import com.b1nd.dodamdodam.nightstudy.domain.ban.entity.NightStudyBanEntity
import com.b1nd.dodamdodam.nightstudy.domain.ban.repository.NightStudyBanRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.UUID

@Service
class NightStudyBanService(
    private val nightStudyBanRepository: NightStudyBanRepository
) {

    fun create(ban: NightStudyBanEntity): NightStudyBanEntity =
        nightStudyBanRepository.save(ban)

    fun getActiveBansByUserId(userId: UUID): List<NightStudyBanEntity> =
        nightStudyBanRepository.findAllByUserIdAndEndedAtGreaterThanEqual(userId, LocalDate.now())

    fun isBanned(userId: UUID): Boolean =
        getActiveBansByUserId(userId).isNotEmpty()

    fun getAllByUserId(userId: UUID): List<NightStudyBanEntity> =
        nightStudyBanRepository.findAllByUserId(userId)

    fun getAll(): List<NightStudyBanEntity> =
        nightStudyBanRepository.findAll()

    fun deleteByUserId(userId: UUID) =
        nightStudyBanRepository.deleteAllByUserId(userId)
}
