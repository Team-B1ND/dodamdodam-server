package b1nd.dodam.restapi.nightstudy.application.data.req;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudy;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProject;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyProjectRoom;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyType;
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
        List<Integer> students
        ) {
        public NightStudyProject toEntity(Student leader) {
                return NightStudyProject.builder()
                        .type(type)
                        .name(name)
                        .description(description)
                        .startAt(startAt)
                        .endAt(endAt)
                        .room(room)
                        .leader(leader)
                        .build();
        }

        public NightStudy toEntity(Student student, NightStudyProject project) {
                return NightStudy.builder()
                        .type(type)
                        .content(description)
                        .doNeedPhone(false)
                        .reasonForPhone(null)
                        .student(student)
                        .project(project)
                        .startAt(startAt)
                        .endAt(endAt)
                        .build();
        }
}
