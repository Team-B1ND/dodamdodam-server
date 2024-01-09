package b1nd.dodamcore.auth.application.dto.response;

import b1nd.dodamcore.auth.application.dto.Token;

public record TokenResponse(int status, String message, Token data) {}