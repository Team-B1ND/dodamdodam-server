package b1nd.dodam.restapi.club.application.data.req;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.club.enumeration.ClubType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateClubReq(
    @Size(max = 14) @NotBlank String name,
    @Size(max = 4) @NotBlank String subject,
    @Size(max = 17) @NotBlank String shortDescription,
    @NotBlank String description,
    @NotNull ClubType type
) {
    public Club toEntity() {
        return Club.builder()
                .name(name)
                .shortDescription(shortDescription)
                .description(description)
                .subject(subject)
                .type(type)
                .state(ClubStatus.PENDING)
                .build();
    }
}
