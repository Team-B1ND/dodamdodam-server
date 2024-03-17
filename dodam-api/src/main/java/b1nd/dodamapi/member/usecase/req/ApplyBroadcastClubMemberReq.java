package b1nd.dodamapi.member.usecase.req;

import b1nd.dodamcore.member.domain.entity.BroadcastClubMember;
import b1nd.dodamcore.member.domain.entity.Member;
import jakarta.validation.constraints.NotBlank;

public record ApplyBroadcastClubMemberReq(@NotBlank String id) {
    public BroadcastClubMember toEntity(Member member) {
        return BroadcastClubMember.builder()
                .member(member)
                .build();
    }
}