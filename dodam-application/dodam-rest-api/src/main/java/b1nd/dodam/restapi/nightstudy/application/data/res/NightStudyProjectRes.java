package b1nd.dodam.restapi.nightstudy.application.data.res;

import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProject;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyProjectRoom;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyType;

import java.time.LocalDate;
import java.util.List;

public record NightStudyProjectRes(Long id,
                                    NightStudyType type,
                                    NightStudyProjectRoom room,
                                    String name,
                                    String description,
                                    LocalDate startAt,
                                    LocalDate endAt,
                                   String leaderName) {
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
                project.getStatus(),
                project.getLeader()
        );
    }

    public static List<NightStudyProjectRes> fromEntityList(List<NightStudyProject> projects) {
        return projects.stream()
                .map(project -> new NightStudyProjectRes(
                        project.getId(),
                        project.getName(),
                        project.getStartAt(),
                        project.getEndAt(),
                        project.getRoom(),
                        project.getType()
                )).toList();
    }
}
