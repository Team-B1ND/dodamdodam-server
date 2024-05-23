package b1nd.dodamapi.outgoing.usecase.dto.req;

import b1nd.dodamcore.member.domain.entity.Student;
import b1nd.dodamcore.outgoing.domain.entity.OutGoing;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ApplyOutGoingReq(@NotBlank String reason, @NotNull LocalDateTime startAt, @NotNull LocalDateTime endAt) {
    public OutGoing toEntity(Student student) {
        return OutGoing.builder()
                .reason(reason)
                .student(student)
                .startAt(startAt)
                .endAt(endAt)
                .build();
    }
}