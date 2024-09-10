package b1nd.dodam.restapi.outsleeping.application;

import b1nd.dodam.core.util.ZonedDateTimeUtil;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.repository.StudentRepository;
import b1nd.dodam.domain.rds.member.repository.TeacherRepository;
import b1nd.dodam.domain.rds.outsleeping.entity.OutSleeping;
import b1nd.dodam.domain.rds.outsleeping.exception.NotOutSleepingApplicantException;
import b1nd.dodam.domain.rds.outsleeping.service.OutSleepingService;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.outsleeping.application.data.req.ApplyOutSleepingReq;
import b1nd.dodam.restapi.outsleeping.application.data.req.RejectOutSleepingReq;
import b1nd.dodam.restapi.outsleeping.application.data.res.OutSleepingRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import b1nd.dodam.restapi.support.pushalarm.ApprovalAlarmEvent;
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
public class OutSleepingUseCase {

    private final OutSleepingService outSleepingService;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final MemberAuthenticationHolder memberAuthenticationHolder;
    private final ApplicationEventPublisher eventPublisher;

    public Response apply(ApplyOutSleepingReq req) {
        OutSleeping outSleeping = req.toEntity(studentRepository.getByMember(memberAuthenticationHolder.current()));
        outSleepingService.save(outSleeping);
        return Response.created("외박 신청 성공");
    }

    public Response cancel(Long id) {
        OutSleeping outSleeping = outSleepingService.getById(id);
        throwExceptionWhenStudentIsNotApplicant(outSleeping);

        outSleepingService.delete(outSleeping);
        return Response.noContent("외박 취소 성공");
    }

    public void throwExceptionWhenStudentIsNotApplicant(OutSleeping o) {
        if(o.isNotApplicant(studentRepository.getByMember(memberAuthenticationHolder.current()))) {
            throw new NotOutSleepingApplicantException();
        }
    }

    public Response allow(Long id) {
        modifyStatus(id, ApprovalStatus.ALLOWED, null);
        return Response.noContent("외박 승인 성공");
    }

    public Response reject(Long id, Optional<RejectOutSleepingReq> req) {
        modifyStatus(id, ApprovalStatus.REJECTED, req.map(RejectOutSleepingReq::rejectReason).orElse(null));
        return Response.noContent("외박 거절 성공");
    }

    public Response revert(Long id) {
        modifyStatus(id, ApprovalStatus.PENDING, null);
        return Response.noContent("외박 대기 성공");
    }

    private void modifyStatus(Long id, ApprovalStatus status, String rejectReason) {
        Member member = memberAuthenticationHolder.current();
        OutSleeping outSleeping = outSleepingService.getById(id);
        outSleeping.modifyStatus(teacherRepository.getByMember(member), status, rejectReason);
        eventPublisher.publishEvent(ApprovalAlarmUtil.createAlarmEvent(
                member.getPushToken(), "외박", outSleeping.getRejectReason(), outSleeping.getStatus()));
    }

    @Transactional(readOnly = true)
    public ResponseData<List<OutSleepingRes>> getValid() {
        LocalDate now = ZonedDateTimeUtil.nowToLocalDate();
        return ResponseData.ok("유효한 외박 조회 성공", OutSleepingRes.of(outSleepingService.getValid(now)));
    }

    @Transactional(readOnly = true)
    public ResponseData<List<OutSleepingRes>> getByEndAt(LocalDate endAt){
        return ResponseData.ok("종료일 기준 외박 조회 성공", OutSleepingRes.of(outSleepingService.getByEndAt(endAt)));
    }

    @Transactional(readOnly = true)
    public ResponseData<List<OutSleepingRes>> getMy() {
        Student student = studentRepository.getByMember(memberAuthenticationHolder.current());
        LocalDate now = ZonedDateTimeUtil.nowToLocalDate();
        return ResponseData.ok("내 외박 조회 성공", OutSleepingRes.of(outSleepingService.getByStudent(student, now)));
    }

}
