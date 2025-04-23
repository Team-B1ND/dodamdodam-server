package b1nd.dodam.restapi.nightstudy.application;

import b1nd.dodam.core.util.ZonedDateTimeUtil;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.entity.Teacher;
import b1nd.dodam.domain.rds.member.enumeration.ActiveStatus;
import b1nd.dodam.domain.rds.member.repository.StudentRepository;
import b1nd.dodam.domain.rds.member.repository.TeacherRepository;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudy;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyBan;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProject;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyType;
import b1nd.dodam.domain.rds.nightstudy.exception.NightStudyDuplicateException;
import b1nd.dodam.domain.rds.nightstudy.exception.NotNightStudyApplicantException;
import b1nd.dodam.domain.rds.nightstudy.service.NightStudyBanService;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class NightStudyUseCase {

    private final NightStudyService nightStudyService;
    private final NightStudyBanService nightStudyBanService;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final MemberAuthenticationHolder memberAuthenticationHolder;
    private final NightStudyProjectService nightStudyProjectService;

    public Response apply(ApplyNightStudyReq req) {
        Student student = studentRepository.getByMember(memberAuthenticationHolder.current());
        nightStudyBanService.validateBan(student);
        throwExceptionWhenDurationIsDuplicate(student, req.startAt(), req.endAt(), req.type());
        nightStudyService.save(req.toEntity(student));
        return Response.created("심야자습 신청 성공");
    }

    public Response applyProject(ApplyNightStudyProjectReq req) {
        Student leader = studentRepository.getByMember(memberAuthenticationHolder.current());
        checkLeaderAndStudentsBanned(leader, req);
        NightStudyProject project = nightStudyProjectService.save(req.toProjectEntity(leader));
        List<Student> students = studentRepository.findAllById(req.students());
        List<NightStudy> nightStudies = createNightStudies(leader, students, project, req);
        nightStudyService.saveAll(nightStudies);
        return Response.created("프로젝트 심야자습 신청 성공");
    }

    private List<NightStudy> createNightStudies(Student leader, List<Student> students, NightStudyProject project, ApplyNightStudyProjectReq req) {
        return Stream.concat(
                Stream.of(req.toEntity(leader).joinProject(project)),
                students.stream().map(student -> req.toEntity(student).joinProject(project))
        ).toList();
    }

    private void checkLeaderAndStudentsBanned(Student leader, ApplyNightStudyProjectReq req) {
        nightStudyBanService.validateBan(leader);
        nightStudyBanService.validateMultipleBans(req.students());
    }

    private void throwExceptionWhenDurationIsDuplicate(Student student, LocalDate startAt, LocalDate endAt, NightStudyType type) {
        if(nightStudyService.checkDurationDuplication(student, startAt, endAt, type)) {
            throw new NightStudyDuplicateException();
        }
    }

    public Response cancel(Long id) {
        Student student = studentRepository.getByMember(memberAuthenticationHolder.current());
        NightStudy nightStudy = nightStudyService.getBy(id);
        throwExceptionWhenStudentIsNotApplicant(nightStudy, student);
        nightStudyService.delete(nightStudy);
        return Response.noContent("심야자습 취소 성공");
    }

    public Response cancelProject(Long projectId) {
        NightStudyProject project = nightStudyProjectService.getBy(projectId);
        Student leader = studentRepository.getByMember(memberAuthenticationHolder.current());
        if (!project.isLeader(leader)) throw new NotNightStudyApplicantException();
        nightStudyService.deleteAllByProject(project);
        nightStudyProjectService.delete(project);
        return Response.ok("프로젝트 심야자습 취소 성공");
    }

    private void throwExceptionWhenStudentIsNotApplicant(NightStudy nightStudy, Student student) {
        if (nightStudy.isApplicant(student)) {
            throw new NotNightStudyApplicantException();
        }
    }

//    @PushAlarmEvent(target = "심야자습", status = ApprovalStatus.ALLOWED)
    public Response allow(Long id) {
        modifyStatus(id, ApprovalStatus.ALLOWED, null);
        return Response.noContent("심야자습 승인 성공");
    }

//    @PushAlarmEvent(target = "심야자습", status = ApprovalStatus.REJECTED)
    public Response reject(Long id, Optional<RejectNightStudyReq> req) {
        modifyStatus(id, ApprovalStatus.REJECTED, req.map(RejectNightStudyReq::rejectReason).orElse(null));
        return Response.noContent("심야자습 거절 성공");
    }

//    @PushAlarmEvent(target = "심야자습", status = ApprovalStatus.PENDING)
    public Response revert(Long id) {
        modifyStatus(id, ApprovalStatus.PENDING, null);
        return Response.noContent("심야자습 대기 성공");
    }

    private void modifyStatus(Long id, ApprovalStatus status, String rejectReason) {
        Member member = memberAuthenticationHolder.current();
        Teacher teacher = teacherRepository.getByMember(member);
        NightStudy nightStudy = nightStudyService.getBy(id);
        nightStudy.modifyStatus(teacher, status, rejectReason);
    }

    private void modifyProjectStatus(Long projectId, ApprovalStatus status, String rejectReason) {
        Member leader = memberAuthenticationHolder.current();
        Teacher teacher = teacherRepository.getByMember(leader);
        NightStudyProject project = nightStudyProjectService.getBy(projectId);
        project.modifyStatus(teacher, status, rejectReason);
        List<NightStudy> nightStudies = nightStudyService.getAllByProject(project);
        for (NightStudy n : nightStudies) n.modifyStatus(teacher, status, rejectReason);
    }

//    @PushAlarmEvent(target = "심야자습", status = ApprovalStatus.ALLOWED)
    public Response allowProject(Long id) {
        modifyProjectStatus(id, ApprovalStatus.ALLOWED, null);
        return Response.noContent("프로젝트 심야자습 승인 성공");
    }

//    @PushAlarmEvent(target = "심야자습", status = ApprovalStatus.REJECTED)
    public Response rejectProject(Long id) {
        modifyProjectStatus(id, ApprovalStatus.REJECTED, null);
        return Response.noContent("프로젝트 심야자습 거절 성공");
    }

//    @PushAlarmEvent(target = "심야자습", status = ApprovalStatus.PENDING)
    public Response revertProject(Long id) {
        modifyProjectStatus(id, ApprovalStatus.PENDING, null);
        return Response.noContent("프로젝트 심야자습 대기 성공");
    }

//    @PushAlarmEvent(target = "심야자습", status = ApprovalStatus.BANNED)
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
    public ResponseData<List<NightStudyRes>> getMy() {
        Student student = studentRepository.getByMember(memberAuthenticationHolder.current());
        LocalDate now = ZonedDateTimeUtil.nowToLocalDate();
        List<NightStudyRes> result = NightStudyRes.of(nightStudyService.getMy(student, now));
        return ResponseData.ok("내 심야자습 조회 성공", result);
    }

    @Transactional(readOnly = true)
    public ResponseData<List<NightStudyRes>> getPending() {
        List<NightStudyRes> result = NightStudyRes.of(nightStudyService.getPending());
        return ResponseData.ok("대기중인 심야자습 조회 성공", result);
    }

    @Transactional(readOnly = true)
    public ResponseData<List<NightStudyRes>> getValid() {
        LocalDate now = ZonedDateTimeUtil.nowToLocalDate();
        List<NightStudyRes> result = NightStudyRes.of(nightStudyService.getValid(now));
        return ResponseData.ok("승인된 심야자습 조회 성공", result);
    }

    @Transactional(readOnly = true)
    public ResponseData<List<StudentWithNightStudyBanRes>> getMembers() {
        LocalDate now = ZonedDateTimeUtil.nowToLocalDate();
        List<Student> students = studentRepository.findAllByMember_Status(ActiveStatus.ACTIVE);
        List<Integer> bannedStudentIds = nightStudyBanService.findAllStudentIdByDate(now);
        return ResponseData.ok("학생 및 정지 여부 조회 성공", StudentWithNightStudyBanRes.of(students, bannedStudentIds));
    }

    @Transactional(readOnly = true)
    public ResponseData<List<NightStudyProjectRes>> getValidProjects() {
        LocalDate today = ZonedDateTimeUtil.nowToLocalDate();
        List<NightStudyProjectRes> result = NightStudyProjectRes.of(nightStudyProjectService.getAllByDateRange(today));
        return ResponseData.ok("유효한 모든 프로젝트 조회 성공", result);
    }

    @Transactional(readOnly = true)
    public ResponseData<List<NightStudyProjectRes>> getPendingProjects() {
        LocalDate today = ZonedDateTimeUtil.nowToLocalDate();
        List<NightStudyProjectRes> result = NightStudyProjectRes.of(nightStudyProjectService.getPendingProjects(today));
        return ResponseData.ok("대기중인 프로젝트 심야자습 조회 성공", result);
    }

    @Transactional(readOnly = true)
    public ResponseData<List<NightStudyProjectRes>> getAllowedProjects() {
        LocalDate today = ZonedDateTimeUtil.nowToLocalDate();
        List<NightStudyProjectRes> result = NightStudyProjectRes.of(nightStudyProjectService.getAllowedProjects(today));
        return ResponseData.ok("승인된 프로젝트 심야자습 조회 성공", result);
    }

    @Transactional(readOnly = true)
    public ResponseData<List<NightStudyProjectRes>> getMyProjects() {
        Student student = studentRepository.getByMember(memberAuthenticationHolder.current());
        LocalDate today = ZonedDateTimeUtil.nowToLocalDate();
        List<NightStudyProjectRes> result = NightStudyProjectRes.of(nightStudyProjectService.getMyProjects(student, today));
        return ResponseData.ok("내 프로젝트 심야자습 조회 성공", result);
    }

    @Transactional(readOnly = true)
    public ResponseData<List<NightStudyProjectRoomRes>> getRoomsInUse() {
        LocalDate today = ZonedDateTimeUtil.nowToLocalDate();
        List<NightStudyProjectRoomRes> result = NightStudyProjectRoomRes.of(nightStudyProjectService.getAllRoomsWithProjects(today));
        return ResponseData.ok("사용중인 프로젝트 실 조회 성공", result);
    }
}
