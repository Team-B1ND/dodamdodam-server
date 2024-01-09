package b1nd.dodamcore.auth.application.dto.response;

import b1nd.dodamcore.member.domain.entity.Member;

public record LoginResponse(Member member, String accessToken, String refreshToken) {}