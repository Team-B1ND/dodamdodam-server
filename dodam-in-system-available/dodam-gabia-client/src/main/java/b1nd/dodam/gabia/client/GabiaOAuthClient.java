package b1nd.dodam.gabia.client;

import b1nd.dodam.client.core.WebClientSupport;
import b1nd.dodam.gabia.client.properties.GabiaProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public final class GabiaOAuthClient {

    private final WebClientSupport webClientSupport;
    private final GabiaProperties gabiaProperties;

    public CompletableFuture<String> getAccessToken() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "client_credentials");

        return webClientSupport.post(
                gabiaProperties.getTokenUrl(),
                formData,
                GabiaOAuthRes.class,
                HttpHeaders.AUTHORIZATION, "Basic " + getAuthValue()
        ).toFuture().thenApply(GabiaOAuthRes::accessToken);
    }

    private String getAuthValue() {
        return Base64.getEncoder().encodeToString(
                String.format(
                        "%s:%s",
                        gabiaProperties.getId(),
                        gabiaProperties.getApiKey()
                ).getBytes(StandardCharsets.UTF_8)
        );
    }

}

record GabiaOAuthRes(
        @JsonProperty("access_token")
        String accessToken,
        @JsonProperty("refresh_token")
        String refreshToken,
        @JsonProperty("expires_in")
        long expiresIn,
        String scope,
        @JsonProperty("create_on")
        String createOn,
        @JsonProperty("is_expires")
        String isExpires,
        @JsonProperty("token_type")
        String tokenType,
        String code
) {}
