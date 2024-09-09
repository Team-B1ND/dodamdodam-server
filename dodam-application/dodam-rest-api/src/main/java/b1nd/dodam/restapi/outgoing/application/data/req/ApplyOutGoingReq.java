package b1nd.dodam.restapi.outgoing.application.data.req;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.outgoing.entity.OutGoing;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ApplyOutGoingReq(
        @NotBlank String reason,
        @NotNull LocalDateTime startAt,
        @NotNull LocalDateTime endAt,
        Boolean isDinner
        ) {
    public OutGoing toEntity(Student student) {
        Boolean dinner = (isDinner != null) ? isDinner : true;
        return OutGoing.builder()
                .reason(reason)
                .student(student)
                .startAt(startAt)
                .endAt(endAt)
                .isDinner(dinner)
                .build();
    }
}
