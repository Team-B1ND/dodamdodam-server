package b1nd.dodam.restapi.club.application.data.res;

import java.util.List;

public record ClubStudentListRes(
        boolean isLeader,
        List<ClubStudentRes> students
) {
    public static ClubStudentListRes of(boolean isLeader, List<ClubStudentRes> students) {
        return new ClubStudentListRes(
            isLeader,
            students
        );
    }
}
