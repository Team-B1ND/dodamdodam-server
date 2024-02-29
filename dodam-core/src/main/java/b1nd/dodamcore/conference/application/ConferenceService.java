package b1nd.dodamcore.conference.application;

import b1nd.dodamcore.conference.application.dto.res.ConferenceRes;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ConferenceService {

    private final ConferenceClient conferenceClient;

    @Async
    public CompletableFuture<List<ConferenceRes>> get() {
        return conferenceClient.get();
    }

}