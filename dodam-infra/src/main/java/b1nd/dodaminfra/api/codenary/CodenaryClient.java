package b1nd.dodaminfra.api.codenary;

import b1nd.dodamcore.common.util.ZonedDateTimeUtil;
import b1nd.dodamcore.conference.application.ConferenceClient;
import b1nd.dodamcore.conference.application.dto.res.ConferenceRes;
import b1nd.dodaminfra.webclient.WebClientSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
final class CodenaryClient implements ConferenceClient {

    private final WebClientSupport webClient;
    private final CodenaryProperties properties;

    @Override
    public CompletableFuture<List<ConferenceRes>> get() {
        LocalDate date = ZonedDateTimeUtil.nowToLocalDate();
        String url = String.format(properties.getUrl(), date.getYear(), date.getMonthValue());

        return webClient.get(url, String.class)
                .flatMap(json -> Mono.fromCallable(() -> CodenaryItemParser.parse(json)))
                .toFuture();
    }

}