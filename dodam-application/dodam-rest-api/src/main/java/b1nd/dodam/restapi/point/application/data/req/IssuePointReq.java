package b1nd.dodam.restapi.point.application.data.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record IssuePointReq(@NotNull LocalDate issueAt, @NotNull int reasonId, @NotEmpty List<Integer> studentIds) {}
