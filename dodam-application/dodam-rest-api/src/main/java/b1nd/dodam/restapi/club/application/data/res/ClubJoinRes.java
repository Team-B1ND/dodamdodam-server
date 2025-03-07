package b1nd.dodam.restapi.club.application.data.res;

import b1nd.dodam.domain.rds.club.entity.ClubMember;
import b1nd.dodam.domain.rds.club.enumeration.ClubPriority;
import b1nd.dodam.domain.rds.club.enumeration.ClubType;

public record ClubJoinRes(
        long clubId,
        ClubPriority clubPriority,
        ClubType clubType
) {
    public static ClubJoinRes of(ClubMember clubMember) {
        return new ClubJoinRes(
                clubMember.getClub().getId(),
                clubMember.getPriority(),
                clubMember.getClub().getType()
        );
    }
}