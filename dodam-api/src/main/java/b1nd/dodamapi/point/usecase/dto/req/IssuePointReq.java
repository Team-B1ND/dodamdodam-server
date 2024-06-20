package b1nd.dodamapi.point.usecase.dto.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record IssuePointReq(@NotNull LocalDate issueAt, @NotNull Integer reasonId, @NotEmpty List<Integer> studentIds) {}
