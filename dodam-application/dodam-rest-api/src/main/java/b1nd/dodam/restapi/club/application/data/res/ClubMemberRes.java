package b1nd.dodam.restapi.club.application.data.res;

import b1nd.dodam.domain.rds.club.entity.ClubMember;
import b1nd.dodam.domain.rds.club.enumeration.ClubPermission;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;

public record ClubMemberRes(
    Long id,
    ClubPermission clubPermission,
    ClubStatus status,
    ClubDetailRes club
) {
    public static ClubMemberRes of(ClubMember clubmember) {
        return new ClubMemberRes(
            clubmember.getId(),
            clubmember.getPermission(),
            clubmember.getClubStatus(),
            ClubDetailRes.of(clubmember.getClub())
        );
    }
}