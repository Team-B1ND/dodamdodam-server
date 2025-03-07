package b1nd.dodam.restapi.club.application.data.req;

import b1nd.dodam.domain.rds.club.entity.ClubTime;
import b1nd.dodam.domain.rds.club.enumeration.ClubTimeType;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record ClubTimeReq(
    @NotNull
    ClubTimeType type,
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate start,
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate end
) {
    public ClubTime toEntity() {
        return ClubTime.builder()
            .id(type)
            .start(start)
            .end(end)
            .build();
    }
}
