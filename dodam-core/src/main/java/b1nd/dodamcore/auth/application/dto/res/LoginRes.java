package b1nd.dodamcore.auth.application.dto.res;

import b1nd.dodamcore.member.domain.entity.Member;

public record LoginRes(Member member, String accessToken, String refreshToken) {}