package b1nd.dodamcore.outsleeping.application.dto.res;

import b1nd.dodamcore.member.domain.vo.StudentRes;
import b1nd.dodamcore.outsleeping.domain.entity.OutSleeping;
import b1nd.dodamcore.outsleeping.domain.enums.OutSleepingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record OutSleepingRes(
        Long id,
        String reason,
        OutSleepingStatus status,
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