package b1nd.dodam.token.client.data.res;

import b1nd.dodam.token.client.data.TokenReqInfo;

public record TokenInfoRes(int status, String message, TokenReqInfo data) {}
