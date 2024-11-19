package b1nd.dodam.restapi.group.application.data.req;

import b1nd.dodam.domain.rds.group.entity.Group;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ManageGroupReq(
        @NotBlank String name,
        @NotNull String description
) {
    public Group toEntity() {
        return Group.builder()
                .name(name)
                .description(description)
                .build();
    }
}
