package com.b1nd.dodamdodam.outsleeping.domain.member.service

import com.b1nd.dodamdodam.outsleeping.domain.member.entity.MemberEntity
import com.b1nd.dodamdodam.outsleeping.domain.member.repository.MemberRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {

    fun createOrUpdate(userId: UUID, name: String, role: String, grade: Int?, room: Int?, number: Int?) {
        val member = memberRepository.findByUserId(userId)
        if (member != null) {
            member.update(name, role, grade, room, number)
        } else {
            memberRepository.save(
                MemberEntity(
                    userId = userId,
                    name = name,
                    role = role,
                    grade = grade,
                    room = room,
                    number = number,
                )
            )
        }
    }

    fun getByUserId(userId: UUID): MemberEntity? =
        memberRepository.findByUserId(userId)

    fun getByUserIds(userIds: Collection<UUID>): Map<UUID, MemberEntity> =
        memberRepository.findAllByUserIdIn(userIds).associateBy { it.userId }

    fun getAllStudents(): List<MemberEntity> =
        memberRepository.findAllByRole("STUDENT")
}
