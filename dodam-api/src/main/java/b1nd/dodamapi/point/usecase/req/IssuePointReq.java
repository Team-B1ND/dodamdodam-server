package b1nd.dodamapi.point.usecase.req;

import b1nd.dodamapi.common.validator.NotEmptyList;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record IssuePointReq(@NotNull LocalDate issueAt, @NotNull Integer reasonId, @NotEmptyList List<Integer> studentIds) {}
