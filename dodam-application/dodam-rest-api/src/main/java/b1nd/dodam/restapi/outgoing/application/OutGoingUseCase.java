package b1nd.dodam.restapi.outgoing.application;

import b1nd.dodam.core.util.ZonedDateTimeUtil;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.enumeration.ActiveStatus;
import b1nd.dodam.domain.rds.member.repository.StudentRepository;
import b1nd.dodam.domain.rds.member.repository.TeacherRepository;
import b1nd.dodam.domain.rds.outgoing.entity.OutGoing;
import b1nd.dodam.domain.rds.outgoing.exception.NotOutGoingApplicantException;
import b1nd.dodam.domain.rds.outgoing.service.OutGoingService;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.outgoing.application.data.req.ApplyOutGoingReq;
import b1nd.dodam.restapi.outgoing.application.data.req.RejectOutGoingReq;
import b1nd.dodam.restapi.outgoing.application.data.res.OutGoingMealCountRes;
import b1nd.dodam.restapi.outgoing.application.data.res.OutGoingRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import b1nd.dodam.restapi.support.pushalarm.PushAlarmEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Component
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class OutGoingUseCase {

    private final OutGoingService outGoingService;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final MemberAuthenticationHolder memberAuthenticationHolder;

    public Response apply(ApplyOutGoingReq req) {
        OutGoing outGoing = req.toEntity(studentRepository.getByMember(memberAuthenticationHolder.current()));
        outGoingService.save(outGoing);
        return Response.created("외출 신청 성공");
    }

    public Response cancel(Long id) {
        OutGoing outGoing = outGoingService.getById(id);
        throwExceptionWhenStudentIsNotApplicant(outGoing);

        outGoingService.delete(outGoing);
        return Response.noContent("외출 취소 성공");
    }

    @Transactional(readOnly = true)
    public ResponseData<OutGoingMealCountRes> getMealDemandDuringOuting(LocalDate date) {
        Long nonEatersCount = outGoingService.getTodayCountByDinnerOrNotAndDate(Boolean.FALSE, date);
        Long eatersCount = studentRepository.countByMemberStatus(ActiveStatus.ACTIVE) - nonEatersCount;
        return ResponseData.ok("외출 중 급식 수요 조회 성공", new OutGoingMealCountRes(eatersCount, nonEatersCount));
    }

    private void throwExceptionWhenStudentIsNotApplicant(OutGoing outGoing) {
        if (outGoing.isNotApplicant(studentRepository.getByMember(memberAuthenticationHolder.current()))) {
            throw new NotOutGoingApplicantException();
        }
    }

    @PushAlarmEvent(target = "외출", status = ApprovalStatus.ALLOWED)
    public Response allow(Long id) {
        modifyStatus(id, ApprovalStatus.ALLOWED, null);
        return Response.noContent("외출 승인 성공");
    }

    @PushAlarmEvent(target = "외출", status = ApprovalStatus.REJECTED)
    public Response reject(Long id, Optional<RejectOutGoingReq> req) {
        modifyStatus(id, ApprovalStatus.REJECTED, req.map(RejectOutGoingReq::rejectReason).orElse(null));
        return Response.noContent("외출 거절 성공");
    }

    @PushAlarmEvent(target = "외출", status = ApprovalStatus.PENDING)
    public Response revert(Long id) {
        modifyStatus(id, ApprovalStatus.PENDING, null);
        return Response.noContent("외출 대기 성공");
    }

    private void modifyStatus(Long id, ApprovalStatus status, String rejectReason) {
        Member member = memberAuthenticationHolder.current();
        OutGoing outGoing = outGoingService.getById(id);
        outGoing.modifyStatus(teacherRepository.getByMember(member), status, rejectReason);
    }

    @Transactional(readOnly = true)
    public ResponseData<List<OutGoingRes>> getByDate(LocalDate date) {
        LocalDateTime startDay = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime endDay = LocalDateTime.of(date, LocalTime.MAX);
        return ResponseData.ok("날짜별 외출 조회 성공", OutGoingRes.of(outGoingService.getByDate(startDay, endDay)));
    }

    @Transactional(readOnly = true)
    public ResponseData<List<OutGoingRes>> getMy() {
        Student student = studentRepository.getByMember(memberAuthenticationHolder.current());
        LocalDateTime now = ZonedDateTimeUtil.nowToLocalDateTime();
        return ResponseData.ok("내 외출 조회 성공", OutGoingRes.of(outGoingService.getByStudent(student, now)));
    }
}
