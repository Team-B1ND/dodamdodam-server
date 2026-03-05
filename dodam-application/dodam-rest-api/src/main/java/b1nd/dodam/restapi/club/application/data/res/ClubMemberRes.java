package b1nd.dodam.restapi.club.application.data.res;

import b1nd.dodam.domain.rds.club.entity.ClubMember;
import b1nd.dodam.domain.rds.club.enumeration.ClubPermission;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;

public record ClubMemberRes(
    long id,
    ClubPermission clubPermission,
    ClubStatus status,
    String introduction,
    ClubDetailRes club
) {
    public static ClubMemberRes of(ClubMember clubMember) {
        return new ClubMemberRes(
            clubMember.getId(),
            clubMember.getPermission(),
            clubMember.getClubStatus(),
            clubMember.getIntroduction(),
            ClubDetailRes.of(clubMember.getClub())
        );
    }
}