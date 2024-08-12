package b1nd.dodam.restapi.auth.application.data.res;

import b1nd.dodam.domain.rds.member.entity.Member;

public record LoginRes(Member member, String accessToken, String refreshToken) {}
