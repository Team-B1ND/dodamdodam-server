package b1nd.dodam.restapi.club.application.data.res;

import b1nd.dodam.domain.rds.club.entity.ClubMember;
import b1nd.dodam.domain.rds.club.enumeration.ClubPermission;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;

public record ClubStudentRes(
    long id,
    ClubStatus status,
    ClubPermission permission,
    int studentId,
    String name,
    int grade,
    int room,
    int number,
    String profileImage
    ) {

    public static ClubStudentRes of(ClubMember clubMember) {
        return new ClubStudentRes(
            clubMember.getId(),
            clubMember.getClubStatus(),
            clubMember.getPermission(),
            clubMember.getStudent().getId(),
            clubMember.getStudent().getMember().getName(),
            clubMember.getStudent().getGrade(),
            clubMember.getStudent().getRoom(),
            clubMember.getStudent().getNumber(),
            clubMember.getStudent().getMember().getProfileImage()
        );
    }
}