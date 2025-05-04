package b1nd.dodam.restapi.nightstudy.application.data.res;

import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProject;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyProjectRoom;

import java.time.LocalDate;
import java.util.List;

public record NightStudyProjectRoomRes(
        NightStudyProjectRoom room,
        String project,
        LocalDate startAt,
        LocalDate endAt
) {
    public static List<NightStudyProjectRoomRes> of(List<NightStudyProject> projects) {
        return projects.stream()
                .map(NightStudyProjectRoomRes::of)
                .toList();
    }

    public static NightStudyProjectRoomRes of(NightStudyProject project) {
        return new NightStudyProjectRoomRes(
                project.getRoom(),
                project.getName(),
                project.getStartAt(),
                project.getEndAt()
        );
    }
}
