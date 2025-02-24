package b1nd.dodam.restapi.club.application.data.res;

import b1nd.dodam.domain.rds.club.entity.ClubMember;
import b1nd.dodam.domain.rds.club.enumeration.ClubPermission;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.restapi.member.application.data.res.StudentRes;

public record ClubStudentRes(
    Long id,
    ClubStatus status,
    ClubPermission permission,
    StudentRes student
) {
    public static ClubStudentRes of(ClubMember clubMember) {
        return new ClubStudentRes(
            clubMember.getId(),
            clubMember.getClubStatus(),
            clubMember.getPermission(),
            StudentRes.of(clubMember.getStudent())
        );
    }
}