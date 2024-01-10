package b1nd.dodamcore.auth.application.dto.response;

public record TokenInfoResponse(int statusCode, String message, TokenInfo data) {
    public record TokenInfo(String memberId, int accessLevel, int apiKeyAccessLevel, int iat, int exp, String iss, String sub) {}
}