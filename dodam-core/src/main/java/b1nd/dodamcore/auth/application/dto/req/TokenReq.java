package b1nd.dodamcore.auth.application.dto.req;

public record TokenReq(String memberId, int accessLevel, int apiKeyAccessLevel) {}