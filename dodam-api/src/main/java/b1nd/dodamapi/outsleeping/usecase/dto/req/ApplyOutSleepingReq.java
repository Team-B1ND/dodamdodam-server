package b1nd.dodamapi.outsleeping.usecase.dto.req;

import b1nd.dodamcore.member.domain.entity.Student;
import b1nd.dodamcore.outsleeping.domain.entity.OutSleeping;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ApplyOutSleepingReq(@NotBlank String reason, @NotNull LocalDate startAt, @NotNull LocalDate endAt) {
    public OutSleeping toEntity(Student student) {
        return OutSleeping.builder()
                .reason(reason)
                .startAt(startAt)
                .endAt(endAt)
                .student(student)
                .build();
    }
}
