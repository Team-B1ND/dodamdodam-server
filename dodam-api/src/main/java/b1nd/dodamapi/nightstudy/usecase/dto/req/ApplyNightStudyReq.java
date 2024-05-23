package b1nd.dodamapi.nightstudy.usecase.dto.req;

import b1nd.dodamcore.common.enums.SchoolPlace;
import b1nd.dodamcore.member.domain.entity.Student;
import b1nd.dodamcore.nightstudy.domain.entity.NightStudy;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ApplyNightStudyReq(@NotNull @Size(min = 10, max = 250) String content,
                                 @NotNull String place,
                                 @NotNull Boolean doNeedPhone, String reasonForPhone,
                                 @NotNull LocalDate startAt, @NotNull LocalDate endAt) {
    public NightStudy toEntity(Student student) {
        return NightStudy.builder()
                .content(content)
                .place(SchoolPlace.of(place))
                .doNeedPhone(doNeedPhone)
                .reasonForPhone(reasonForPhone)
                .student(student)
                .startAt(startAt)
                .endAt(endAt)
                .build();
    }
}