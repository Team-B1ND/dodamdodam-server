package com.b1nd.dodamdodam.club.application.club.data

import com.b1nd.dodamdodam.club.application.club.data.request.ClubTimeRequest
import com.b1nd.dodamdodam.club.application.club.data.request.CreateClubRequest
import com.b1nd.dodamdodam.club.application.club.data.request.JoinClubMemberRequest
import com.b1nd.dodamdodam.club.domain.club.entity.ClubEntity
import com.b1nd.dodamdodam.club.domain.club.entity.ClubMemberEntity
import com.b1nd.dodamdodam.club.domain.club.entity.ClubTimeEntity
import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubPermission
import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubStatus

fun CreateClubRequest.toClubEntity(): ClubEntity =
    ClubEntity(
        name = name,
        shortDescription = shortDescription,
        description = description,
        image = image,
        subject = subject,
        type = type,
        state = ClubStatus.WAITING,
    )

fun JoinClubMemberRequest.toClubMemberEntity(studentId: Long, club: ClubEntity): ClubMemberEntity =
    ClubMemberEntity(
        permission = ClubPermission.CLUB_MEMBER,
        clubStatus = ClubStatus.PENDING,
        priority = clubPriority,
        studentId = studentId,
        club = club,
        introduction = introduction,
    )

fun ClubTimeRequest.toClubTimeEntity(): ClubTimeEntity =
    ClubTimeEntity(
        id = type,
        start = start,
        end = end,
    )
