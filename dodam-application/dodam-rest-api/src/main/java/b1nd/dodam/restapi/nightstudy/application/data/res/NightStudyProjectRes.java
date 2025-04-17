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
                                    LocalDate endAt) {
    public static List<NightStudyProjectRes> of(List<NightStudyProject> nightStudyProjects) {
        return nightStudyProjects.parallelStream()
                .map(NightStudyProjectRes::of)
                .toList();
    }

    public static NightStudyProjectRes of(NightStudyProject nightStudyProject) {
        return new NightStudyProjectRes(
                nightStudyProject.getId(),
                nightStudyProject.getType(),
                nightStudyProject.getRoom(),
                nightStudyProject.getName(),
                nightStudyProject.getDescription(),
                nightStudyProject.getStartAt(),
                nightStudyProject.getEndAt()
        );
    }
}
