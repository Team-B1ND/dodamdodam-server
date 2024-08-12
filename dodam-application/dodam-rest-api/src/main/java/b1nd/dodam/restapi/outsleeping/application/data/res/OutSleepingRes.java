package b1nd.dodam.restapi.outsleeping.application.data.res;

import b1nd.dodam.domain.rds.outsleeping.entity.OutSleeping;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import b1nd.dodam.restapi.member.application.data.res.StudentRes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record OutSleepingRes(
        Long id,
        String reason,
        ApprovalStatus status,
        LocalDate startAt,
        LocalDate endAt,
        StudentRes student,
        String rejectReason,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt) {
    public static List<OutSleepingRes> of(List<OutSleeping> outSleepings) {
        return outSleepings.stream()
                .map(OutSleepingRes::of)
                .toList();
    }

    public static OutSleepingRes of(OutSleeping outSleeping) {
        return new OutSleepingRes(
                outSleeping.getId(),
                outSleeping.getReason(),
                outSleeping.getStatus(),
                outSleeping.getStartAt(),
                outSleeping.getEndAt(),
                StudentRes.of(outSleeping.getStudent()),
                outSleeping.getRejectReason(),
                outSleeping.getCreatedAt(),
                outSleeping.getModifiedAt()
        );
    }
}
