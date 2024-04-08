package b1nd.dodamcore.conference.application;

import b1nd.dodamcore.conference.application.dto.res.ConferenceRes;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ConferenceClient {

    CompletableFuture<List<ConferenceRes>> get();

}
