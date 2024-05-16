package b1nd.dodamapi.outgoing.usecase.dto.res;

import b1nd.dodamcore.member.domain.vo.StudentRes;
import b1nd.dodamcore.outgoing.domain.entity.OutGoing;
import b1nd.dodamcore.outgoing.domain.enums.OutGoingStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OutGoingRes(Long id, String reason, OutGoingStatus status, LocalDateTime startAt, LocalDateTime endAt,
                          StudentRes student, String rejectReason, LocalDateTime createdAt, LocalDateTime modifiedAt) {
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
                o.getCreatedAt(),
                o.getModifiedAt()
        );
    }
}