package com.b1nd.dodamdodam.member.domain.member.repository

import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
import com.b1nd.dodamdodam.member.domain.member.entity.MemberEntity
import com.b1nd.dodamdodam.member.domain.member.enumeration.ActiveStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface MemberRepository : JpaRepository<MemberEntity, Long> {
    fun findByUsername(username: String): MemberEntity?
    fun existsByUsername(username: String): Boolean
    fun existsByEmail(email: String): Boolean
    fun findByStatus(status: ActiveStatus): List<MemberEntity>

    @Query("SELECT m FROM MemberEntity m JOIN MemberRoleEntity mr ON mr.member = m WHERE mr.role = :role AND m.status = :status AND m.name LIKE %:name%")
    fun searchByNameAndRoleAndStatus(
        @Param("name") name: String,
        @Param("role") role: RoleType,
        @Param("status") status: ActiveStatus,
        pageable: Pageable
    ): Page<MemberEntity>

    @Query("SELECT DISTINCT m FROM MemberEntity m LEFT JOIN MemberRoleEntity mr ON mr.member = m WHERE (:name IS NULL OR m.name LIKE %:name%) AND (:role IS NULL OR mr.role = :role) AND (:status IS NULL OR m.status = :status)")
    fun searchMembers(
        @Param("name") name: String?,
        @Param("role") role: RoleType?,
        @Param("status") status: ActiveStatus?,
        pageable: Pageable
    ): Page<MemberEntity>
}
