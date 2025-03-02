package b1nd.dodam.restapi.club.application.data.res;

import b1nd.dodam.domain.rds.club.entity.ClubMember;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.club.enumeration.ClubType;

public record ClubStatusRes(
        long id,
        String name,
        ClubType type,
        ClubStatus myStatus
) {
    public static ClubStatusRes of(ClubMember clubMember) {
        return new ClubStatusRes(
                clubMember.getId(),
                clubMember.getClub().getName(),
                clubMember.getClub().getType(),
                clubMember.getClubStatus()
        );
    }
}
