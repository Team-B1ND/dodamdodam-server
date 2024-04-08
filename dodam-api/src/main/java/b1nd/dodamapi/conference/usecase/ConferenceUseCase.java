package b1nd.dodamapi.conference.usecase;

import b1nd.dodamapi.common.response.ResponseData;
import b1nd.dodamcore.conference.application.ConferenceClient;
import b1nd.dodamcore.conference.application.dto.res.ConferenceRes;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class ConferenceUseCase {

    private final ConferenceClient client;

    @Async
    public CompletableFuture<ResponseData<List<ConferenceRes>>> get() {
        return client.get().thenApply(res -> ResponseData.ok("컨퍼런스 조회 성공", res));
    }

}
