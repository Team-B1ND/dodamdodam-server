package b1nd.dodamcore.auth.application.dto.request;

public record TokenRequest(String memberId, int accessLevel, int apiKeyAccessLevel) {}