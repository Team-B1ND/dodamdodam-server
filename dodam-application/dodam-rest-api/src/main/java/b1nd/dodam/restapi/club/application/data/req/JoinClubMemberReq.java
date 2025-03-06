package b1nd.dodam.restapi.club.application.data.req;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.entity.ClubMember;
import b1nd.dodam.domain.rds.club.enumeration.ClubPermission;
import b1nd.dodam.domain.rds.club.enumeration.ClubPriority;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.member.entity.Student;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record JoinClubMemberReq(
        @NotNull Long clubId,
        ClubPriority clubPriority,
        String introduction) {

    public ClubMember toEntity(Student student, Club club) {
        return ClubMember.builder()
                .permission(ClubPermission.CLUB_MEMBER)
                .clubStatus(ClubStatus.PENDING)
                .priority(clubPriority)
                .student(student)
                .club(club)
                .introduction(introduction)
                .build();
    }
}
