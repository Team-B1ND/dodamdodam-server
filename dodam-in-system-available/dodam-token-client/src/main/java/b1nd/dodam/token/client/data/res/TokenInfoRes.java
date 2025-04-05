package b1nd.dodam.token.client.data.res;

import b1nd.dodam.token.client.data.TokenInfo;

public record TokenInfoRes(int status, String message, TokenInfo data) {}
