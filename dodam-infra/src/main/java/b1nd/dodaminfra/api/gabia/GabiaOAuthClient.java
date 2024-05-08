package b1nd.dodaminfra.api.gabia;

import b1nd.dodaminfra.webclient.WebClientSupport;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

@Component
@RequiredArgsConstructor
final class GabiaOAuthClient {

    private final WebClientSupport webClientSupport;
    private final GabiaProperties gabiaProperties;

    public String getAccessToken() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "client_credentials");

        return Objects.requireNonNull(
                webClientSupport.post(
                        gabiaProperties.getTokenUrl(),
                        formData,
                        GabiaOAuthRes.class,
                        HttpHeaders.AUTHORIZATION, "Basic " + getAuthValue()
                ).block()
        ).accessToken();
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
