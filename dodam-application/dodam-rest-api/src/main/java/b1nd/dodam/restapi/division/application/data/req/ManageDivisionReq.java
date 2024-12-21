package b1nd.dodam.restapi.division.application.data.req;

import b1nd.dodam.domain.rds.division.entity.Division;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ManageDivisionReq(
        @NotBlank String name,
        @NotNull String description
) {
    public Division toEntity() {
        return Division.builder()
                .name(name)
                .description(description)
                .build();
    }
}
