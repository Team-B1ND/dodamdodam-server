package b1nd.dodam.restapi.member.application.data.req;

import b1nd.dodam.domain.rds.member.entity.BroadcastClubMember;
import b1nd.dodam.domain.rds.member.entity.Member;
import jakarta.validation.constraints.NotBlank;

public record ApplyBroadcastClubMemberReq(@NotBlank String id) {
    public BroadcastClubMember toEntity(Member member) {
        return BroadcastClubMember.builder()
                .member(member)
                .build();
    }
}
