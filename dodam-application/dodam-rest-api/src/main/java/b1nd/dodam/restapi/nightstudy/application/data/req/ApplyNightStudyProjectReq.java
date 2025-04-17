package b1nd.dodam.restapi.nightstudy.application.data.req;

import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProject;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyProjectRoom;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyType;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record ApplyNightStudyProjectReq(
        @NotNull NightStudyType type,
        @NotNull String name,
        @NotNull String description,
        @NotNull LocalDate startAt,
        @NotNull LocalDate endAt,
        @NotNull NightStudyProjectRoom room,
        @NotBlank List<Integer> students,
        @NotNull ApprovalStatus status
        ) {
        public NightStudyProject toEntity(List<Integer> students) {
                return NightStudyProject.builder()
                        .type(type)
                        .name(name)
                        .description(description)
                        .startAt(startAt)
                        .endAt(endAt)
                        .room(room)
                        .student(students)
                        .build();
        }
}
