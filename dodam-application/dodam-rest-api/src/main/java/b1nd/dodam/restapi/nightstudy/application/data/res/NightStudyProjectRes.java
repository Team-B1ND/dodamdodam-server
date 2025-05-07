package b1nd.dodam.restapi.nightstudy.application.data.res;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProject;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProjectMember;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyProjectMemberRole;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyProjectRoom;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyProjectType;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import b1nd.dodam.restapi.member.application.data.res.StudentWithImageRes;

import java.time.LocalDate;
import java.util.List;

public record NightStudyProjectRes(
        Long id,
        NightStudyProjectType type,
        ApprovalStatus status,
        NightStudyProjectRoom room,
        String name,
        String description,
        LocalDate startAt,
        LocalDate endAt
) {
    public static NightStudyProjectRes of(NightStudyProject project) {
        return new NightStudyProjectRes(
                project.getId(),
                project.getType(),
                project.getStatus(),
                project.getRoom(),
                project.getName(),
                project.getDescription(),
                project.getStartAt(),
                project.getEndAt()
        );
    }
}
