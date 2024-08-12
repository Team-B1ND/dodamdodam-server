package b1nd.dodam.token.client.data.res;

import b1nd.dodam.token.client.data.Token;

public record TokenRes(int status, String message, Token data) {}
