package b1nd.dodam.restapi.club.application.data.res;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.club.enumeration.ClubType;
import b1nd.dodam.restapi.member.application.data.res.TeacherRes;

public record ClubDetailRes(
    long id,
    String name,
    String shortDescription,
    String description,
    String subject,
    String image,
    ClubType type,
    TeacherRes teacher,
    ClubStatus state,
    int maxMemberCount
) {
    public static ClubDetailRes of(Club club) {
        if (club == null || club.getName().isEmpty()) {
            return null;
        }

        return new ClubDetailRes(
            club.getId(),
            club.getName(),
            club.getShortDescription(),
            club.getDescription(),
            club.getSubject(),
            club.getImage(),
            club.getType(),
            TeacherRes.of(club.getTeacher()),
            club.getState(),
            club.getMaxMemberCount()
        );
    }
}
