package b1nd.dodam.restapi.nightstudy.application;

import b1nd.dodam.core.util.ZonedDateTimeUtil;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.entity.Teacher;
import b1nd.dodam.domain.rds.member.repository.StudentRepository;
import b1nd.dodam.domain.rds.member.repository.TeacherRepository;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudy;
import b1nd.dodam.domain.rds.nightstudy.exception.NightStudyDuplicateException;
import b1nd.dodam.domain.rds.nightstudy.exception.NotNightStudyApplicantException;
import b1nd.dodam.domain.rds.nightstudy.service.NightStudyService;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.nightstudy.application.data.req.ApplyNightStudyReq;
import b1nd.dodam.restapi.nightstudy.application.data.req.RejectNightStudyReq;
import b1nd.dodam.restapi.nightstudy.application.data.res.NightStudyRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import b1nd.dodam.restapi.support.pushalarm.ApprovalAlarmUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class NightStudyUseCase {

    private final NightStudyService nightStudyService;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final MemberAuthenticationHolder memberAuthenticationHolder;
    private final ApplicationEventPublisher eventPublisher;

    public Response apply(ApplyNightStudyReq req) {
        Student student = studentRepository.getByMember(memberAuthenticationHolder.current());
        throwExceptionWhenDurationIsDuplicate(student, req.startAt(), req.endAt());
        nightStudyService.save(req.toEntity(student));
        return Response.created("심야자습 신청 성공");
    }

    private void throwExceptionWhenDurationIsDuplicate(Student student, LocalDate startAt, LocalDate endAt) {
        if(nightStudyService.checkDurationDuplication(student, startAt, endAt)) {
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

    private void throwExceptionWhenStudentIsNotApplicant(NightStudy nightStudy, Student student) {
        if(nightStudy.isApplicant(student)) {
            throw new NotNightStudyApplicantException();
        }
    }

    public Response allow(Long id) {
        modifyStatus(id, ApprovalStatus.ALLOWED, null);
        return Response.noContent("심야자습 승인 성공");
    }

    public Response reject(Long id, Optional<RejectNightStudyReq> req) {
        modifyStatus(id, ApprovalStatus.REJECTED, req.map(RejectNightStudyReq::rejectReason).orElse(null));
        return Response.noContent("심야자습 거절 성공");
    }

    public Response revert(Long id) {
        modifyStatus(id, ApprovalStatus.PENDING, null);
        return Response.noContent("심야자습 대기 성공");
    }

    private void modifyStatus(Long id, ApprovalStatus status, String rejectReason) {
        Member member = memberAuthenticationHolder.current();
        Teacher teacher = teacherRepository.getByMember(member);
        NightStudy nightStudy = nightStudyService.getBy(id);
        nightStudy.modifyStatus(teacher, status, rejectReason);
        eventPublisher.publishEvent(ApprovalAlarmUtil.createAlarmEvent(
                member.getPushToken(), "심야자습", nightStudy.getRejectReason(), nightStudy.getStatus()));
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

}
