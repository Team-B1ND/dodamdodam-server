package b1nd.dodam.restapi.nightstudy.application.data.res;

import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProject;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProjectMember;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyProjectRoom;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyProjectType;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;

import java.time.LocalDate;
import java.util.List;

public record NightStudyProjectWithMembersRes(
    Long id,
    NightStudyProjectType type,
    ApprovalStatus status,
    NightStudyProjectRoom room,
    String name,
    String description,
    LocalDate startAt,
    LocalDate endAt,
    List<StudentWithProjectRoleRes> members

) {
    public static NightStudyProjectWithMembersRes of(NightStudyProject project, List<NightStudyProjectMember> members) {
        return new NightStudyProjectWithMembersRes(
                project.getId(),
                project.getType(),
                project.getStatus(),
                project.getRoom(),
                project.getName(),
                project.getDescription(),
                project.getStartAt(),
                project.getEndAt(),
                members.stream()
                        .map(StudentWithProjectRoleRes::of)
                        .toList()
        );
    }
}
