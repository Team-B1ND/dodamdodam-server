package com.b1nd.dodamdodam.member.application.member

import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
import com.b1nd.dodamdodam.member.application.member.data.request.ApplyBroadcastClubMemberRequest
import com.b1nd.dodamdodam.member.application.member.data.request.ApplyDormitoryManageMemberRequest
import com.b1nd.dodamdodam.member.application.member.data.request.JoinStudentRequest
import com.b1nd.dodamdodam.member.application.member.data.request.JoinTeacherRequest
import com.b1nd.dodamdodam.member.application.member.data.request.UpdateMemberInfoRequest
import com.b1nd.dodamdodam.member.application.member.data.request.UpdateStudentInfoRequest
import com.b1nd.dodamdodam.member.application.member.data.request.UpdateTeacherForAdminRequest
import com.b1nd.dodamdodam.member.application.member.data.response.MemberInfoResponse
import com.b1nd.dodamdodam.member.application.member.data.toMemberEntity
import com.b1nd.dodamdodam.member.application.member.data.toStudentEntity
import com.b1nd.dodamdodam.member.application.member.data.toTeacherEntity
import com.b1nd.dodamdodam.member.domain.broadcastclub.entity.BroadcastClubMemberEntity
import com.b1nd.dodamdodam.member.domain.broadcastclub.service.BroadcastClubMemberService
import com.b1nd.dodamdodam.member.domain.dormitory.entity.DormitoryManageMemberEntity
import com.b1nd.dodamdodam.member.domain.dormitory.service.DormitoryManageMemberService
import com.b1nd.dodamdodam.member.domain.member.entity.MemberEntity
import com.b1nd.dodamdodam.member.domain.member.entity.MemberRoleEntity
import com.b1nd.dodamdodam.member.domain.member.enumeration.ActiveStatus
import com.b1nd.dodamdodam.member.domain.member.exception.DuplicateBroadcastClubMemberException
import com.b1nd.dodamdodam.member.domain.member.exception.DuplicateDormitoryManageMemberException
import com.b1nd.dodamdodam.member.domain.member.exception.DuplicateEmailException
import com.b1nd.dodamdodam.member.domain.member.exception.DuplicateUsernameException
import com.b1nd.dodamdodam.member.domain.member.service.MemberService
import com.b1nd.dodamdodam.member.domain.parent.service.ParentService
import com.b1nd.dodamdodam.member.domain.student.service.StudentService
import com.b1nd.dodamdodam.member.domain.teacher.service.TeacherService
import org.springframework.data.domain.PageRequest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(rollbackFor = [Exception::class])
class MemberUseCase(
    private val memberService: MemberService,
    private val studentService: StudentService,
    private val teacherService: TeacherService,
    private val parentService: ParentService,
    private val broadcastClubMemberService: BroadcastClubMemberService,
    private val dormitoryManageMemberService: DormitoryManageMemberService,
    private val passwordEncoder: PasswordEncoder,
) {

    fun joinStudent(request: JoinStudentRequest) {
        if (memberService.existsByUsername(request.id)) {
            throw DuplicateUsernameException()
        }
        val encodedPassword = passwordEncoder.encode(request.password)
        val member = memberService.create(request.toMemberEntity(encodedPassword))
        studentService.create(request.toStudentEntity(member))
        memberService.createRole(MemberRoleEntity(member = member, role = RoleType.STUDENT))
    }

    fun joinTeacher(request: JoinTeacherRequest) {
        if (memberService.existsByUsername(request.id)) {
            throw DuplicateUsernameException()
        }
        val encodedPassword = passwordEncoder.encode(request.password)
        val member = memberService.create(request.toMemberEntity(encodedPassword))
        teacherService.create(request.toTeacherEntity(member))
        memberService.createRole(MemberRoleEntity(member = member, role = RoleType.TEACHER))
    }

    @Transactional(readOnly = true)
    fun getMemberInfo(memberId: Long): MemberInfoResponse {
        val member = memberService.getById(memberId)
        return buildMemberInfoResponse(member)
    }

    @Transactional(readOnly = true)
    fun getMyInfo(memberId: Long): MemberInfoResponse {
        val member = memberService.getById(memberId)
        return buildMemberInfoResponse(member)
    }

    @Transactional(readOnly = true)
    fun getMemberIdByUsername(username: String): Long {
        val member = memberService.getByUsername(username)
        return member.id!!
    }

    @Transactional(readOnly = true)
    fun searchMembers(
        name: String?,
        grade: Int?,
        role: RoleType?,
        status: ActiveStatus?,
        page: Int,
        pageSize: Int,
    ): List<MemberInfoResponse> {
        val pageable = PageRequest.of(page, pageSize)
        val members = memberService.searchMembers(name, role, status, pageable)
        val result = members.content.map { buildMemberInfoResponse(it) }
        return if (grade != null) {
            result.filter { response ->
                response.student?.grade == grade
            }
        } else {
            result
        }
    }

    @Transactional(readOnly = true)
    fun searchMembersWithInfo(
        name: String,
        role: RoleType,
        status: ActiveStatus,
        grade: Int?,
        page: Int,
        pageSize: Int,
    ): List<MemberInfoResponse> {
        val pageable = PageRequest.of(page, pageSize)
        val members = memberService.searchByNameAndRoleAndStatus(name, role, status, pageable)
        val result = members.content.map { buildMemberInfoResponse(it) }
        return if (grade != null) {
            result.filter { response ->
                response.student?.grade == grade
            }
        } else {
            result
        }
    }

    @Transactional(readOnly = true)
    fun getMembersByStatus(status: ActiveStatus): List<MemberInfoResponse> =
        memberService.findByStatus(status).map { buildMemberInfoResponse(it) }

    @Transactional(readOnly = true)
    fun getAllActiveMembers(): List<MemberInfoResponse> =
        memberService.findByStatus(ActiveStatus.ACTIVE).map { buildMemberInfoResponse(it) }

    fun updateMemberStatus(memberId: Long, status: ActiveStatus) {
        val member = memberService.getById(memberId)
        member.updateStatus(status)
    }

    fun updateMemberInfo(memberId: Long, request: UpdateMemberInfoRequest) {
        val member = memberService.getById(memberId)
        if (request.email != null && request.email != member.email && memberService.existsByEmail(request.email)) {
            throw DuplicateEmailException()
        }
        member.updateInfo(request.name, request.email, request.phone, request.profileImage)
    }

    fun updateStudentInfo(memberId: Long, request: UpdateStudentInfoRequest) {
        val member = memberService.getById(memberId)
        val student = studentService.getByMember(member)
        student.updateInfo(request.grade, request.room, request.number)
    }

    fun updateTeacherInfoForAdmin(memberId: Long, request: UpdateTeacherForAdminRequest) {
        val member = memberService.getById(memberId)
        val teacher = teacherService.getByMember(member)
        teacher.updateInfo(request.tel, request.position)
    }

    fun deleteMember(memberId: Long) {
        val member = memberService.getById(memberId)
        memberService.delete(member)
    }

    fun applyBroadcastClubMember(request: ApplyBroadcastClubMemberRequest) {
        val member = memberService.getById(request.id)
        if (broadcastClubMemberService.existsByMember(member)) {
            throw DuplicateBroadcastClubMemberException()
        }
        broadcastClubMemberService.create(BroadcastClubMemberEntity(member = member))
        memberService.createRole(MemberRoleEntity(member = member, role = RoleType.BROADCASTER))
    }

    fun removeBroadcastClubMember(request: ApplyBroadcastClubMemberRequest) {
        val member = memberService.getById(request.id)
        val broadcastClubMember = broadcastClubMemberService.getByMember(member)
        broadcastClubMemberService.delete(broadcastClubMember)
        val role = memberService.findRole(member, RoleType.BROADCASTER)
        if (role != null) {
            memberService.deleteRoles(member)
            memberService.getRoles(member)
                .filter { it.role != RoleType.BROADCASTER }
                .forEach { memberService.createRole(MemberRoleEntity(member = member, role = it.role)) }
        }
    }

    @Transactional(readOnly = true)
    fun checkBroadcastClubMember(memberId: Long): Boolean {
        val member = memberService.getById(memberId)
        return broadcastClubMemberService.existsByMember(member)
    }

    fun applyDormitoryManageMember(request: ApplyDormitoryManageMemberRequest) {
        val member = memberService.getById(request.id)
        if (dormitoryManageMemberService.existsByMember(member)) {
            throw DuplicateDormitoryManageMemberException()
        }
        dormitoryManageMemberService.create(DormitoryManageMemberEntity(member = member))
        memberService.createRole(MemberRoleEntity(member = member, role = RoleType.DORMITORY_MANAGER))
    }

    fun removeDormitoryManageMember(request: ApplyDormitoryManageMemberRequest) {
        val member = memberService.getById(request.id)
        val dormitoryManageMember = dormitoryManageMemberService.getByMember(member)
        dormitoryManageMemberService.delete(dormitoryManageMember)
        val role = memberService.findRole(member, RoleType.DORMITORY_MANAGER)
        if (role != null) {
            memberService.deleteRoles(member)
            memberService.getRoles(member)
                .filter { it.role != RoleType.DORMITORY_MANAGER }
                .forEach { memberService.createRole(MemberRoleEntity(member = member, role = it.role)) }
        }
    }

    @Transactional(readOnly = true)
    fun checkDormitoryManageMember(memberId: Long): Boolean {
        val member = memberService.getById(memberId)
        return dormitoryManageMemberService.existsByMember(member)
    }

    fun deactivateSelf(memberId: Long) {
        val member = memberService.getById(memberId)
        member.updateStatus(ActiveStatus.DEACTIVATED)
    }

    fun deactivateGraduates() {
        val seniors = studentService.findByGrade(3)
        seniors.forEach { student ->
            student.member.updateStatus(ActiveStatus.DEACTIVATED)
        }
    }

    private fun buildMemberInfoResponse(member: MemberEntity): MemberInfoResponse {
        val roles = memberService.getRoles(member)
        val student = studentService.findByMember(member)
        val teacher = teacherService.findByMember(member)
        return MemberInfoResponse.fromEntity(member, roles, student, teacher)
    }
}
