package b1nd.dodam.codenary.client;

import b1nd.dodam.client.core.WebClientSupport;
import b1nd.dodam.codenary.client.data.res.ConferenceRes;
import b1nd.dodam.codenary.client.properties.CodenaryProperties;
import b1nd.dodam.codenary.client.support.CodenaryItemParser;
import b1nd.dodam.core.util.ZonedDateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class CodenaryClient {

    private final WebClientSupport webClient;
    private final CodenaryProperties properties;

    public CompletableFuture<List<ConferenceRes>> getByMonth() {
        LocalDate date = ZonedDateTimeUtil.nowToLocalDate();
        String url = String.format(properties.getUrl(), date.getYear(), date.getMonthValue());

        return webClient.get(url, String.class)
                .flatMap(json -> Mono.fromCallable(() -> CodenaryItemParser.parse(json)))
                .toFuture();
    }

}
