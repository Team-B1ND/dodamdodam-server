package com.b1nd.dodamdodam.club.domain.club.repository

import com.b1nd.dodamdodam.club.domain.club.entity.ClubEntity
import com.b1nd.dodamdodam.club.domain.club.entity.ClubMemberEntity
import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubPermission
import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubStatus
import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubType
import org.springframework.data.jpa.repository.JpaRepository

interface ClubMemberRepository : JpaRepository<ClubMemberEntity, Long> {
    fun findByIdAndStudentId(id: Long, studentId: Long): ClubMemberEntity?
    fun findByClubAndPermissionAndClubStatus(club: ClubEntity, permission: ClubPermission, clubStatus: ClubStatus): ClubMemberEntity?
    fun findAllByClubAndClubStatus(club: ClubEntity, clubStatus: ClubStatus): List<ClubMemberEntity>
    fun findAllByClubAndPermission(club: ClubEntity, permission: ClubPermission): List<ClubMemberEntity>
    fun findByClub(club: ClubEntity): List<ClubMemberEntity>
    fun findByClubAndClubStatusNot(club: ClubEntity, clubStatus: ClubStatus): List<ClubMemberEntity>
    fun findByClubAndStudentIdAndClubStatusNot(club: ClubEntity, studentId: Long, clubStatus: ClubStatus): ClubMemberEntity?
    fun findByClubAndStudentIdAndPermission(club: ClubEntity, studentId: Long, permission: ClubPermission): ClubMemberEntity?
    fun existsByClubAndStudentIdAndPermission(club: ClubEntity, studentId: Long, permission: ClubPermission): Boolean
    fun findAllByPermission(permission: ClubPermission): List<ClubMemberEntity>
    fun findByStudentIdAndClubStatus(studentId: Long, clubStatus: ClubStatus): List<ClubMemberEntity>
    fun findByStudentIdAndClubStatusAndClub_State(studentId: Long, clubStatus: ClubStatus, state: ClubStatus): List<ClubMemberEntity>
    fun findByStudentIdAndPermissionAndClub_StateNot(studentId: Long, permission: ClubPermission, state: ClubStatus): List<ClubMemberEntity>
    fun findByStudentIdAndClubStatusInAndClub_TypeAndClub_State(studentId: Long, clubStatuses: List<ClubStatus>, type: ClubType, state: ClubStatus): List<ClubMemberEntity>
    fun findByClubInAndClubStatusNotIn(clubs: List<ClubEntity>, statuses: List<ClubStatus>): List<ClubMemberEntity>
    fun findByStudentIdInAndClubStatusNotAndClub_Type(studentIds: List<Long>, clubStatus: ClubStatus, type: ClubType): List<ClubMemberEntity>
    fun findByStudentIdInAndClubStatusAndClub_TypeAndClub_StateNot(studentIds: List<Long>, clubStatus: ClubStatus, type: ClubType, state: ClubStatus): List<ClubMemberEntity>
    fun findAllByStudentIdAndPermissionAndClub_Type(studentId: Long, permission: ClubPermission, type: ClubType): List<ClubMemberEntity>
    fun findByClubAndClubStatus(club: ClubEntity, clubStatus: ClubStatus): List<ClubMemberEntity>
    fun findByStudentIdAndClubStatusInAndClub_Type(studentId: Long, statuses: List<ClubStatus>, type: ClubType): List<ClubMemberEntity>
}
