package b1nd.dodam.restapi.club.application.data.res;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.entity.ClubMember;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.club.enumeration.ClubType;
import b1nd.dodam.restapi.member.application.data.res.TeacherRes;

public record ClubWithLeaderRes(
    long id,
    String name,
    String shortDescription,
    String description,
    String subject,
    String image,
    ClubType type,
    TeacherRes teacher,
    ClubStatus state,
    int requiredMember,
    ClubStudentRes leader
) {
    public static ClubWithLeaderRes of(ClubMember clubMember) {
        Club club = clubMember.getClub();
        if (club == null || club.getState() == ClubStatus.DELETED) {
            return null;
        }

        return new ClubWithLeaderRes(
                club.getId(),
                club.getName(),
                club.getShortDescription(),
                club.getDescription(),
                club.getSubject(),
                club.getImage(),
                club.getType(),
                TeacherRes.of(club.getTeacher()),
                club.getState(),
                club.getRequiredMember(),
                ClubStudentRes.of(clubMember)
        );
    }
}