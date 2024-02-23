package b1nd.dodamcore.point.application.dto.req;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record IssuePointReq(@NotNull LocalDate issueAt, @NotNull Integer reasonId, List<Integer> studentIds) {}