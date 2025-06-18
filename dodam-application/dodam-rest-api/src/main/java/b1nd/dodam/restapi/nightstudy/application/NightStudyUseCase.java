package b1nd.dodam.restapi.nightstudy.application;

import b1nd.dodam.domain.rds.member.entity.DormitoryManageMember;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.entity.Teacher;
import b1nd.dodam.domain.rds.member.enumeration.ActiveStatus;
import b1nd.dodam.domain.rds.member.enumeration.MemberRole;
import b1nd.dodam.domain.rds.member.repository.DormitoryManageMemberRepository;
import b1nd.dodam.domain.rds.member.repository.StudentRepository;
import b1nd.dodam.domain.rds.member.repository.TeacherRepository;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudy;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyBan;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProject;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProjectMember;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyProjectMemberRole;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyProjectRoom;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyProjectType;
import b1nd.dodam.domain.rds.nightstudy.exception.NotNightStudyApplicantException;
import b1nd.dodam.domain.rds.nightstudy.service.NightStudyBanService;
import b1nd.dodam.domain.rds.nightstudy.service.NightStudyProjectMemberService;
import b1nd.dodam.domain.rds.nightstudy.service.NightStudyProjectService;
import b1nd.dodam.domain.rds.nightstudy.service.NightStudyService;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.nightstudy.application.data.req.ApplyNightStudyProjectReq;
import b1nd.dodam.restapi.nightstudy.application.data.req.ApplyNightStudyReq;
import b1nd.dodam.restapi.nightstudy.application.data.req.BanNightStudyReq;
import b1nd.dodam.restapi.nightstudy.application.data.req.RejectNightStudyReq;
import b1nd.dodam.restapi.nightstudy.application.data.res.*;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import b1nd.dodam.restapi.support.pushalarm.PushAlarmEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class NightStudyUseCase {
    private final NightStudyService nightStudyService;
    private final NightStudyBanService nightStudyBanService;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final MemberAuthenticationHolder authenticationHolder;
    private final MemberAuthenticationHolder memberAuthenticationHolder;
    private final NightStudyProjectService nightStudyProjectService;
    private final NightStudyProjectMemberService nightStudyProjectMemberService;
    private final DormitoryManageMemberRepository dormitoryManageMemberRepository;

    public Response apply(ApplyNightStudyReq req) {
        Student student = studentRepository.getByMember(memberAuthenticationHolder.current());
        validateBanned(student, req);
        nightStudyService.save(req.toEntity(student));
        return Response.created("심야자습 신청 성공");
    }

    private void validateBanned(Student student, ApplyNightStudyReq req) {
        nightStudyBanService.validateBan(student);
        nightStudyService.validateDurationDuplication(student, req.startAt(), req.endAt());
    }

    public Response applyProject(ApplyNightStudyProjectReq req) {
        Student leader = studentRepository.getByMember(memberAuthenticationHolder.current());
        List<Student> students = studentRepository.getByIds(req.students());
        checkLeaderAndStudentsBanned(leader, students, req);
        NightStudyProject project = nightStudyProjectService.save(req.toEntity());
        nightStudyProjectMemberService.saveAll(getProjectMembers(leader, students, project));
        return Response.created("프로젝트 심야자습 신청 성공");
    }

    private void checkLeaderAndStudentsBanned(Student leader, List<Student> students, ApplyNightStudyProjectReq req) {
        List<Student> participants = Stream.concat(Stream.of(leader), students.stream()).toList();
        nightStudyBanService.validateMultipleBans(participants);
        if (req.type() == NightStudyProjectType.NIGHT_STUDY_PROJECT_2) nightStudyService.validateNoActiveNightStudies(participants);
    }

    private List<NightStudyProjectMember> getProjectMembers(Student leader, List<Student> students, NightStudyProject project) {
        List<NightStudyProjectMember> members = students.stream()
            .map(s -> NightStudyProjectMember.toMember(s, project, NightStudyProjectMemberRole.MEMBER))
            .collect(Collectors.toList());
        members.add(NightStudyProjectMember.toMember(leader, project, NightStudyProjectMemberRole.LEADER));
        return members;
    }

    public Response cancel(Long id) {
        Student student = studentRepository.getByMember(memberAuthenticationHolder.current());
        NightStudy nightStudy = nightStudyService.getBy(id);
        throwExceptionWhenStudentIsNotApplicant(nightStudy, student);
        nightStudyService.delete(nightStudy);
        return Response.noContent("심야자습 취소 성공");
    }

    private void throwExceptionWhenStudentIsNotApplicant(NightStudy nightStudy, Student student) {
        if (nightStudy.isApplicant(student)) {
            throw new NotNightStudyApplicantException();
        }
    }

    public Response cancelProject(Long projectId) {
        NightStudyProject project = nightStudyProjectService.getById(projectId);
        Student student = studentRepository.getByMember(memberAuthenticationHolder.current());
        NightStudyProjectMember leader = nightStudyProjectMemberService.findByStudentAndProject(student, project);
        if (leader.getRole() != NightStudyProjectMemberRole.LEADER) throw new NotNightStudyApplicantException();
        nightStudyProjectService.delete(project);
        return Response.ok("프로젝트 심야자습 취소 성공");
    }

    @PushAlarmEvent(target = "심야자습", status = ApprovalStatus.ALLOWED)
    public Response allow(Long id) {
        modifyStatus(id, ApprovalStatus.ALLOWED, null);
        return Response.noContent("심야자습 승인 성공");
    }

    @PushAlarmEvent(target = "심야자습", status = ApprovalStatus.REJECTED)
    public Response reject(Long id, Optional<RejectNightStudyReq> req) {
        modifyStatus(id, ApprovalStatus.REJECTED, req.map(RejectNightStudyReq::rejectReason).orElse(null));
        return Response.noContent("심야자습 거절 성공");
    }

    @PushAlarmEvent(target = "심야자습", status = ApprovalStatus.PENDING)
    public Response revert(Long id) {
        modifyStatus(id, ApprovalStatus.PENDING, null);
        return Response.noContent("심야자습 대기 성공");
    }

    private void modifyStatus(Long id, ApprovalStatus status, String rejectReason) {
        Member member = memberAuthenticationHolder.current();
        NightStudy nightStudy = nightStudyService.getBy(id);
        if (member.getRole() == MemberRole.TEACHER) {
            Teacher teacher = teacherRepository.getByMember(member);
            nightStudy.modifyStatusByTeacher(teacher, status, rejectReason);
            return;
        }
        if (member.getRole() == MemberRole.STUDENT) {
            DormitoryManageMember dormitoryManageMember = dormitoryManageMemberRepository.getByMember(member);
            nightStudy.modifyStatusByDormitoryManageMember(dormitoryManageMember, status, rejectReason);
        }
    }

    public Response allowProject(Long id, NightStudyProjectRoom room) {
        modifyProjectStatus(id, ApprovalStatus.ALLOWED, room, null);
        return Response.noContent("프로젝트 심야자습 승인 성공");
    }

    public Response rejectProject(Long id, RejectNightStudyReq req) {
        modifyProjectStatus(id, ApprovalStatus.REJECTED, null, req.rejectReason());
        return Response.noContent("프로젝트 심야자습 거절 성공");
    }

    public Response revertProject(Long id) {
        modifyProjectStatus(id, ApprovalStatus.PENDING, null, null);
        return Response.noContent("프로젝트 심야자습 대기 성공");
    }

    private void modifyProjectStatus(Long id, ApprovalStatus status, NightStudyProjectRoom room, String rejectReason) {
        Member member = memberAuthenticationHolder.current();
        NightStudyProject project = nightStudyProjectService.getById(id);
        if (member.getRole() == MemberRole.TEACHER) {
            Teacher teacher = teacherRepository.getByMember(member);
            project.modifyStatusByTeacher(teacher, status, room, rejectReason);
            return;
        }
        if (member.getRole() == MemberRole.STUDENT) {
            DormitoryManageMember dormitoryManageMember = dormitoryManageMemberRepository.getByMember(member);
            project.modifyStatusByDormitoryManageMember(dormitoryManageMember, status, room, rejectReason);
        }
    }

    public Response applyBan(BanNightStudyReq req) {
        Student student = studentRepository.getById(req.student());
        nightStudyService.rejectAllByStudent(student);
        nightStudyBanService.updateBan(student, req.reason(), LocalDate.now(), req.ended());
        return Response.ok("심야자습 정지 등록 성공");
    }

    public Response cancelBan(int id) {
        Student student = studentRepository.getById(id);
        NightStudyBan ban = nightStudyBanService.findUserBan(student);
        nightStudyBanService.delete(ban);
        return Response.ok("심야자습 정지 취소 성공");
    }

    @Transactional(readOnly = true)
    public Response getMyBan() {
        Student student = studentRepository.getByMember(memberAuthenticationHolder.current());
        NightStudyBanRes result;
        result = NightStudyBanRes.of(nightStudyBanService.findByStudent(student));
        return ResponseData.ok("내 심야자습 정지 여부 조회 성공", result);
    }

    @Transactional(readOnly = true)
    public ResponseData<List<NightStudyBanRes>> getAllActiveBans() {
        List<NightStudyBanRes> result = NightStudyBanRes.of(nightStudyBanService.getAllActiveBans());
        return ResponseData.ok("유효한 심야자습 정지 학생 조회 성공", result);
    }

    @Transactional(readOnly = true)
    public ResponseData<List<NightStudyRes>> getAll() {
        List<NightStudyRes> result = NightStudyRes.of(nightStudyService.getAll());
        return ResponseData.ok("모든 심야자습 조회 성공", result);
    }

    @Transactional(readOnly = true)
    public ResponseData<List<NightStudyRes>> getMy() {
        Student student = studentRepository.getByMember(memberAuthenticationHolder.current());
        List<NightStudyRes> result = NightStudyRes.of(nightStudyService.getMy(student));
        return ResponseData.ok("내 심야자습 조회 성공", result);
    }

    @Transactional(readOnly = true)
    public ResponseData<List<NightStudyRes>> getPending() {
        List<NightStudyRes> result = NightStudyRes.of(nightStudyService.getPending());
        return ResponseData.ok("대기중인 심야자습 조회 성공", result);
    }

    @Transactional(readOnly = true)
    public ResponseData<List<NightStudyRes>> getValid() {
        List<NightStudyRes> result = NightStudyRes.of(nightStudyService.getValid());
        return ResponseData.ok("승인된 심야자습 조회 성공", result);
    }

    @Transactional(readOnly = true)
    public ResponseData<List<StudentWithNightStudyBanRes>> getMembers() {
        List<Student> students = studentRepository.findAllByMember_Status(ActiveStatus.ACTIVE);
        List<Integer> bannedStudentIds = nightStudyBanService.findAllStudentIdByDate();
        return ResponseData.ok("학생 및 정지 여부 조회 성공", StudentWithNightStudyBanRes.of(students, bannedStudentIds));
    }

    @Transactional(readOnly = true)
    public ResponseData<NightStudyProjectWithMembersRes> getProjectDetails(Long id) {
        NightStudyProject project = nightStudyProjectService.getById(id);
        List<NightStudyProjectMember> members = nightStudyProjectMemberService.getByProject(project);
        return ResponseData.ok("프로젝트 상세 조회 성공", NightStudyProjectWithMembersRes.of(project, members));
    }

    @Transactional(readOnly = true)
    public ResponseData<List<NightStudyProjectRes>> getValidProjects() {
        List<NightStudyProjectRes> result = nightStudyProjectService.getAllByDateRange().stream()
            .map(NightStudyProjectRes::of)
            .toList();
        return ResponseData.ok("유효한 모든 프로젝트 조회 성공", result);
    }

    @Transactional(readOnly = true)
    public ResponseData<List<NightStudyProjectWithMembersRes>> getPendingProjects() {
        List<NightStudyProjectMember> members = nightStudyProjectMemberService.getPendingProjectMembers();
        return ResponseData.ok("대기중인 프로젝트 심야자습 조회 성공", convertToProjectWithMembersRes(members));
    }

    @Transactional(readOnly = true)
    public ResponseData<List<NightStudyProjectWithMembersRes>> getAllowedProjects() {
        List<NightStudyProjectMember> members = nightStudyProjectMemberService.getAllowedProjectMembers();
        return ResponseData.ok("승인된 프로젝트 심야자습 조회 성공", convertToProjectWithMembersRes(members));
    }

    private List<NightStudyProjectWithMembersRes> convertToProjectWithMembersRes(List<NightStudyProjectMember> members) {
        return members.stream()
                .collect(Collectors.groupingBy(NightStudyProjectMember::getProject))
                .entrySet().stream()
                .map(entry -> NightStudyProjectWithMembersRes.of(entry.getKey(), entry.getValue()))
                .toList();
    }

    @Transactional(readOnly = true)
    public ResponseData<List<NightStudyProjectRes>> getMyProjects() {
        Student student = studentRepository.getByMember(memberAuthenticationHolder.current());
        List<NightStudyProjectRes> result = nightStudyProjectMemberService.findByStudent(student).stream()
            .map(NightStudyProjectRes::of)
            .toList();
        return ResponseData.ok("내 프로젝트 심야자습 조회 성공", result);
    }

    @Transactional(readOnly = true)
    public ResponseData<List<NightStudyProjectRoomRes>> getRoomsInUse() {
        List<NightStudyProjectRoomRes> result = NightStudyProjectRoomRes.of(nightStudyProjectService.getAllRoomsWithProjects());
        return ResponseData.ok("사용중인 프로젝트 실 조회 성공", result);
    }

    @Transactional(readOnly = true)
    public ResponseData<List<StudentWithNightStudyBanRes>> getStudentsWithBan() {
        Member member = authenticationHolder.current();
        List<Student> students = studentRepository.findAllByMember_StatusAndMemberNot(ActiveStatus.ACTIVE, member);
        List<Integer> bannedStudents = nightStudyBanService.findAllStudentIdByDate();
        List<StudentWithNightStudyBanRes> result = StudentWithNightStudyBanRes.of(students, bannedStudents);
        return ResponseData.ok("학생 조회 성공", result);
    }

    @Transactional(readOnly = true)
    public ResponseData<NightStudyAndProjectRes> getCombined() {
        List<NightStudyProjectMember> members = nightStudyProjectMemberService.getAllStudentByDate();
        List<NightStudyRes> nightStudyRes = NightStudyRes.of(nightStudyService.getCombinedStudy());
        List<StudentWithNightStudyProjectRes> projectStudents = convertToProjectStudentRes(members);
        NightStudyAndProjectRes result = NightStudyAndProjectRes.of(nightStudyRes, projectStudents);
        return ResponseData.ok("심야자습 및 프로젝트 참가자 조회 성공", result);
    }

    @Transactional(readOnly = true)
    public ResponseData<List<StudentWithNightStudyProjectRes>> getProjectStudents() {
        List<NightStudyProjectMember> members = nightStudyProjectMemberService.getAllStudentByDate();
        return ResponseData.ok("프로젝트 참가 학생 조회 성공", convertToProjectStudentRes(members));
    }

    private List<StudentWithNightStudyProjectRes> convertToProjectStudentRes(List<NightStudyProjectMember> members) {
        return members.stream()
                .map(member -> StudentWithNightStudyProjectRes.of(member.getStudent(), member.getProject()))
                .toList();
    }
}
