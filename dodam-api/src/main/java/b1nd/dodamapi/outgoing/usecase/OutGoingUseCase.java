package b1nd.dodamapi.outgoing.usecase;

import b1nd.dodamapi.common.response.Response;
import b1nd.dodamapi.common.response.ResponseData;
import b1nd.dodamcore.common.util.ZonedDateTimeUtil;
import b1nd.dodamcore.member.application.MemberService;
import b1nd.dodamcore.member.domain.entity.Student;
import b1nd.dodamcore.outgoing.application.OutGoingService;
import b1nd.dodamcore.outgoing.application.dto.req.ApplyOutGoingReq;
import b1nd.dodamcore.outgoing.application.dto.req.RejectOutGoingReq;
import b1nd.dodamcore.outgoing.application.dto.res.OutGoingRes;
import b1nd.dodamcore.outgoing.domain.entity.OutGoing;
import b1nd.dodamcore.outgoing.domain.enums.OutGoingStatus;
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
    private final MemberService memberService;

    public Response apply(ApplyOutGoingReq req) {
        OutGoing outGoing = req.toEntity(memberService.getStudentFromSession());
        outGoingService.save(outGoing);
        return Response.created("외출 신청 성공");
    }

    public Response cancel(Long id) {
        OutGoing outGoing = outGoingService.getById(id);
        outGoingService.delete(outGoing);
        return Response.noContent("외출 취소 성공");
    }

    public Response allow(Long id) {
        modifyStatus(id, OutGoingStatus.ALLOWED, null);
        return Response.noContent("외출 승인 성공");
    }

    public Response reject(Long id, Optional<RejectOutGoingReq> req) {
        modifyStatus(id, OutGoingStatus.REJECTED, req.map(RejectOutGoingReq::rejectReason).orElse(null));
        return Response.noContent("외출 거절 성공");
    }

    public Response revert(Long id) {
        modifyStatus(id, OutGoingStatus.PENDING, null);
        return Response.noContent("외출 대기 성공");
    }

    private void modifyStatus(Long id, OutGoingStatus status, String rejectReason) {
        OutGoing outGoing = outGoingService.getById(id);
        outGoing.modifyStatus(memberService.getTeacherFromSession(), status, rejectReason);
    }

    @Transactional(readOnly = true)
    public ResponseData<List<OutGoingRes>> getByDate(LocalDate date) {
        LocalDateTime startDay = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime endDay = LocalDateTime.of(date, LocalTime.MAX);
        return ResponseData.ok("날짜별 외출 조회 성공", OutGoingRes.of(outGoingService.getByDate(startDay, endDay)));
    }

    @Transactional(readOnly = true)
    public ResponseData<List<OutGoingRes>> getMy() {
        Student student = memberService.getStudentFromSession();
        LocalDateTime now = ZonedDateTimeUtil.nowToLocalDateTime();
        return ResponseData.ok("내 외출 조회 성공", OutGoingRes.of(outGoingService.getByStudent(student, now)));
    }

}
