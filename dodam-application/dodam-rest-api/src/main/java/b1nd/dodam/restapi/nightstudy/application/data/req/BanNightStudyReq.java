package b1nd.dodam.restapi.nightstudy.application.data.req;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyBan;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record BanNightStudyReq(
    @NotNull
    Long id,
    @NotNull
    Student student,
    @NotBlank
    String reason,
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate started,
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate ended
) {
    public NightStudyBan toEntity(Student target) {
        return NightStudyBan.builder()
                .student(target)
                .reason(reason)
                .started(started)
                .ended(ended)
                .build();
    }
}
