package b1nd.dodam.restapi.outgoing.application.data.res;

import b1nd.dodam.domain.rds.outgoing.entity.OutGoing;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import b1nd.dodam.restapi.member.application.data.res.StudentRes;

import java.time.LocalDateTime;
import java.util.List;

public record OutGoingRes(Long id, String reason, ApprovalStatus status, LocalDateTime startAt, LocalDateTime endAt,
                          StudentRes student, String rejectReason, Boolean dinnerOrNot, LocalDateTime createdAt, LocalDateTime modifiedAt) {
    public static List<OutGoingRes> of(List<OutGoing> oList) {
        return oList.parallelStream()
                .map(OutGoingRes::of)
                .toList();
    }

    public static OutGoingRes of(OutGoing o) {
        return new OutGoingRes(
                o.getId(),
                o.getReason(),
                o.getStatus(),
                o.getStartAt(),
                o.getEndAt(),
                StudentRes.of(o.getStudent()),
                o.getRejectReason(),
                o.getDinnerOrNot(),
                o.getCreatedAt(),
                o.getModifiedAt()
        );
    }
}
