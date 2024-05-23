package b1nd.dodaminfra.api.gabia;

import b1nd.dodamcore.sms.application.SMSClient;
import b1nd.dodamcore.sms.application.dto.req.SendSmsReq;
import b1nd.dodaminfra.webclient.WebClientSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@RequiredArgsConstructor
public final class GabiaSMSClient implements SMSClient {

    private static final int SMS_MAX_LENGTH = 89;

    private final GabiaOAuthClient gabiaOAuthClient;
    private final WebClientSupport webClientSupport;
    private final GabiaProperties gabiaProperties;

    @Override
    public void send(SendSmsReq req) {
        final String url;
        final MultiValueMap<String, String> formData;

        if(isLMS(req.content().getBytes())) {
            url = gabiaProperties.getLmsUrl();
            formData = createFormData(req);
        } else {
            url = gabiaProperties.getSmsUrl();
            formData = createFormDataWithSubject(req);
        }

        getAuthValue().thenAccept(value -> webClientSupport.post(url, formData, HttpHeaders.AUTHORIZATION, value));
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

    private CompletableFuture<String> getAuthValue() {
        return gabiaOAuthClient.getAccessToken()
                .thenApply(token -> "Basic " + Base64.getEncoder().encodeToString(
                                String.format("%s:%s", gabiaProperties.getId(), token
                                ).getBytes(StandardCharsets.UTF_8)
                        )
                );
    }

}
