package b1nd.dodam.restapi.nightstudy.application.data.res;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProject;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyProjectRoom;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyType;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;

import java.time.LocalDate;
import java.util.List;

public record NightStudyProjectRes(Long id,
                                   NightStudyType type,
                                   NightStudyProjectRoom room,
                                   String name,
                                   String description,
                                   LocalDate startAt,
                                   LocalDate endAt,
                                   Student leader,
                                   ApprovalStatus status
                                   ) {
    public static List<NightStudyProjectRes> of(List<NightStudyProject> nightStudyProjects) {
        return nightStudyProjects.parallelStream()
                .map(NightStudyProjectRes::of)
                .toList();
    }

    public static NightStudyProjectRes of(NightStudyProject project) {
        return new NightStudyProjectRes(
                project.getId(),
                project.getType(),
                project.getRoom(),
                project.getName(),
                project.getDescription(),
                project.getStartAt(),
                project.getEndAt(),
                project.getLeader(),
                project.getStatus()
        );
    }
}
