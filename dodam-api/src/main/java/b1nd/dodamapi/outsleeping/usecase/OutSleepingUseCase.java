package b1nd.dodamapi.outsleeping.usecase;

import b1nd.dodamapi.common.response.Response;
import b1nd.dodamapi.common.response.ResponseData;
import b1nd.dodamcore.common.util.ZonedDateTimeUtil;
import b1nd.dodamcore.member.application.MemberService;
import b1nd.dodamcore.member.domain.entity.Student;
import b1nd.dodamcore.outsleeping.application.OutSleepingService;
import b1nd.dodamapi.outsleeping.usecase.dto.req.ApplyOutSleepingReq;
import b1nd.dodamapi.outsleeping.usecase.dto.req.RejectOutSleepingReq;
import b1nd.dodamapi.outsleeping.usecase.dto.res.OutSleepingRes;
import b1nd.dodamcore.outsleeping.domain.entity.OutSleeping;
import b1nd.dodamcore.outsleeping.domain.enums.OutSleepingStatus;
import b1nd.dodamcore.outsleeping.domain.exception.NotOutSleepingApplicantException;
import lombok.RequiredArgsConstructor;
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
    private final MemberService memberService;

    public Response apply(ApplyOutSleepingReq req) {
        OutSleeping outSleeping = req.toEntity(memberService.getStudentFromSession());
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
        if(o.isNotApplicant(memberService.getStudentFromSession())) {
            throw new NotOutSleepingApplicantException();
        }
    }

    public Response allow(Long id) {
        modifyStatus(id, OutSleepingStatus.ALLOWED, null);
        return Response.noContent("외박 승인 성공");
    }

    public Response reject(Long id, Optional<RejectOutSleepingReq> req) {
        modifyStatus(id, OutSleepingStatus.REJECTED, req.map(RejectOutSleepingReq::rejectReason).orElse(null));
        return Response.noContent("외박 거절 성공");
    }

    public Response revert(Long id) {
        modifyStatus(id, OutSleepingStatus.PENDING, null);
        return Response.noContent("외박 대기 성공");
    }

    private void modifyStatus(Long id, OutSleepingStatus status, String rejectReason) {
        OutSleeping outSleeping = outSleepingService.getById(id);
        outSleeping.modifyStatus(memberService.getTeacherFromSession(), status, rejectReason);
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
        Student student = memberService.getStudentFromSession();
        LocalDate now = ZonedDateTimeUtil.nowToLocalDate();
        return ResponseData.ok("내 외박 조회 성공", OutSleepingRes.of(outSleepingService.getByStudent(student, now)));
    }

}
