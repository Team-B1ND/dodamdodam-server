package b1nd.dodam.restapi.outsleeping.application.data.req;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.outsleeping.entity.OutSleeping;
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
