package b1nd.dodam.restapi.group.application.data.req;

import b1nd.dodam.domain.rds.group.entity.Group;
import jakarta.validation.constraints.NotBlank;

public record ManageGroupReq(
        @NotBlank String name
) {
    public Group toEntity() {
        return Group.builder().name(name).build();
    }
}
