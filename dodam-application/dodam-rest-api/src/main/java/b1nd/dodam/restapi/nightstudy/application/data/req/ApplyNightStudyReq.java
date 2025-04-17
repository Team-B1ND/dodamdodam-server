package b1nd.dodam.restapi.nightstudy.application.data.req;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudy;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ApplyNightStudyReq(@NotNull NightStudyType type,
                                 @NotNull @Size(min = 10, max = 250) String content,
                                 @NotNull Boolean doNeedPhone, String reasonForPhone,
                                 @NotNull LocalDate startAt, @NotNull LocalDate endAt) {
    public NightStudy toEntity(Student student) {
        return NightStudy.builder()
                .type(type)
                .content(content)
                .doNeedPhone(doNeedPhone)
                .reasonForPhone(reasonForPhone)
                .student(student)
                .startAt(startAt)
                .endAt(endAt)
                .build();
    }
}
