package b1nd.dodam.restapi.club.application.data.res;

import b1nd.dodam.domain.rds.club.entity.ClubMember;
import b1nd.dodam.domain.rds.club.enumeration.ClubPriority;
import b1nd.dodam.restapi.member.application.data.res.StudentRes;

public record ClubJoinStudentRes(
        StudentRes student,
        ClubPriority priority
) {
    public static ClubJoinStudentRes of(ClubMember clubMember) {
        return new ClubJoinStudentRes(
            StudentRes.of(clubMember.getStudent()),
            clubMember.getPriority()
        );
    }
}
