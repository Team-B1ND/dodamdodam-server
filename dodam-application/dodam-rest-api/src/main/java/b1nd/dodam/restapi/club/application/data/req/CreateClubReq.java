package b1nd.dodam.restapi.club.application.data.req;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.enumeration.ClubType;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateClubReq(
    @NotBlank String name,
    @NotNull String subject,
    @NotBlank String shortDescription,
    String description,
    ClubType type
    //TODO:: 멤버 리스트
) {
    public Club toEntity() {
        return Club.builder()
                .name(name)
                .shortDescription(shortDescription)
                .description(description)
                .subject(subject)
                .type(type)
                .state(ApprovalStatus.PENDING)
                .build();
    }
}
