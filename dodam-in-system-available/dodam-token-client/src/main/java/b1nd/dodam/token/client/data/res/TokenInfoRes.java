package b1nd.dodam.token.client.data.res;

public record TokenInfoRes(int statusCode, String message, TokenInfo data) {
    public record TokenInfo(String memberId, int accessLevel, int apiKeyAccessLevel, int iat, int exp, String iss, String sub) {}
}
