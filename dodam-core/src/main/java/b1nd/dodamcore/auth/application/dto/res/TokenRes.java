package b1nd.dodamcore.auth.application.dto.res;

import b1nd.dodamcore.auth.application.dto.Token;

public record TokenRes(int status, String message, Token data) {}