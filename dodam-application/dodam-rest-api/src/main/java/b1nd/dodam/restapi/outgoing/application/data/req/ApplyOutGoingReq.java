package b1nd.dodam.restapi.outgoing.application.data.req;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.outgoing.entity.OutGoing;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ApplyOutGoingReq(
        @NotBlank String reason,
        @NotNull LocalDateTime startAt,
        @NotNull LocalDateTime endAt,
        Boolean dinnerOrNot
        ) {
    public OutGoing toEntity(Student student) {
        Boolean dinner = (dinnerOrNot != null) ? dinnerOrNot : true;
        return OutGoing.builder()
                .reason(reason)
                .student(student)
                .startAt(startAt)
                .endAt(endAt)
                .dinnerOrNot(dinner)
                .build();
    }
}
