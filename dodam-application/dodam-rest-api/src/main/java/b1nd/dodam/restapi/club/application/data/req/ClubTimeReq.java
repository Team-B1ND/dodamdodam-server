package b1nd.dodam.restapi.club.application.data.req;

import b1nd.dodam.domain.rds.club.entity.ClubTime;
import b1nd.dodam.domain.rds.club.enumeration.ClubTimeType;

import java.time.LocalDate;

public record ClubTimeReq(
    ClubTimeType type,
    LocalDate start,
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
