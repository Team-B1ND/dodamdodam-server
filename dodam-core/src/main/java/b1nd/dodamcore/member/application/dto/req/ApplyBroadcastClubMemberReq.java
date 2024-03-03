package b1nd.dodamcore.member.application.dto.req;

import jakarta.validation.constraints.NotBlank;

public record ApplyBroadcastClubMemberReq(@NotBlank String id) {}