package b1nd.dodam.gabia.client;

import b1nd.dodam.client.core.WebClientSupport;
import b1nd.dodam.gabia.client.data.req.SendSmsReq;
import b1nd.dodam.gabia.client.properties.GabiaProperties;
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
public class GabiaSMSClient {

    private static final int SMS_MAX_LENGTH = 89;

    private final GabiaOAuthClient gabiaOAuthClient;
    private final WebClientSupport webClientSupport;
    private final GabiaProperties gabiaProperties;

    public void send(SendSmsReq req) {
        try {
            final String url;
            final MultiValueMap<String, String> formData;
            if (isLMS(req.content().getBytes())) {
                url = gabiaProperties.getLmsUrl();
                formData = createFormData(req);
            } else {
                url = gabiaProperties.getSmsUrl();
                formData = createFormDataWithSubject(req);
            }
            getAuthValue().thenAccept(value -> webClientSupport.post(url, formData, HttpHeaders.AUTHORIZATION, value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isLMS(byte[] bytes) {
        return bytes.length > SMS_MAX_LENGTH;
    }

    private MultiValueMap<String, String> createFormData(SendSmsReq req) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("phone", req.receiver());
        map.add("callback", gabiaProperties.getSender());
        map.add("message", req.content());
        map.add("refkey", gabiaProperties.getRefKey());

        return map;
    }

    private MultiValueMap<String, String> createFormDataWithSubject(SendSmsReq req) {
        MultiValueMap<String, String> formData = createFormData(req);
        formData.add("subject", gabiaProperties.getSubject());

        return formData;
    }

    public CompletableFuture<String> getAuthValue() {
        return gabiaOAuthClient.getAccessToken()
                .thenApply(token -> "Basic " + Base64.getEncoder().encodeToString(
                                String.format("%s:%s", gabiaProperties.getId(), token
                                ).getBytes(StandardCharsets.UTF_8)
                        )
                );
    }

}
