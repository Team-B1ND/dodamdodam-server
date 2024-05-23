package b1nd.dodamapi.nightstudy.usecase;

import b1nd.dodamapi.common.response.Response;
import b1nd.dodamapi.common.response.ResponseData;
import b1nd.dodamcore.common.util.ZonedDateTimeUtil;
import b1nd.dodamcore.member.application.MemberService;
import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodamcore.member.domain.entity.Student;
import b1nd.dodamcore.member.domain.entity.Teacher;
import b1nd.dodamcore.nightstudy.application.NightStudyService;
import b1nd.dodamapi.nightstudy.usecase.dto.req.ApplyNightStudyReq;
import b1nd.dodamapi.nightstudy.usecase.dto.req.RejectNightStudyReq;
import b1nd.dodamapi.nightstudy.usecase.dto.res.NightStudyRes;
import b1nd.dodamcore.nightstudy.domain.entity.NightStudy;
import b1nd.dodamcore.nightstudy.domain.enums.NightStudyStatus;
import b1nd.dodamcore.nightstudy.domain.exception.NightStudyDuplicateException;
import b1nd.dodamcore.nightstudy.domain.exception.NotNightStudyApplicantException;
import b1nd.dodamcore.pushmessage.application.FCMSender;
import lombok.RequiredArgsConstructor;
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
    public final MemberService memberService;
    private final FCMSender fcmSender;


    public Response apply(ApplyNightStudyReq req) {
        Student student = memberService.getStudentFromSession();
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
        Student student = memberService.getStudentFromSession();
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
        NightStudy nightStudy = modifyStatus(id, NightStudyStatus.ALLOWED, null);
        fcmSender.sendToMember(nightStudy.getStudent().getMember(),
                "도담도담","선생님께서 심야자습을 승인하셨어요.");
        return Response.noContent("심야자습 승인 성공");
    }

    public Response reject(Long id, Optional<RejectNightStudyReq> req) {
        NightStudy nightStudy = modifyStatus(id, NightStudyStatus.REJECTED,
                req.map(RejectNightStudyReq::rejectReason).orElse(null));
        fcmSender.sendToMember(nightStudy.getStudent().getMember(),
                "도담도담","선생님께서 심야신청을 거절하셨어요.\n다시 신청해주세요.");
        return Response.noContent("심야자습 거절 성공");
    }

    public Response revert(Long id) {
        modifyStatus(id, NightStudyStatus.PENDING, null);
        return Response.noContent("심야자습 대기 성공");
    }

    private NightStudy modifyStatus(Long id, NightStudyStatus status, String rejectReason) {
        Teacher teacher = memberService.getTeacherFromSession();
        NightStudy nightStudy = nightStudyService.getBy(id);
        nightStudy.modifyStatus(teacher, status, rejectReason);
        return nightStudy;
    }

    @Transactional(readOnly = true)
    public ResponseData<List<NightStudyRes>> getMy() {
        Student student = memberService.getStudentFromSession();
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
