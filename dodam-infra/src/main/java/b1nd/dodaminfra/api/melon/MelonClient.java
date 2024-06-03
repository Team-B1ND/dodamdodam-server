package b1nd.dodaminfra.api.melon;

import b1nd.dodamcore.wakeupsong.application.MusicChartClient;
import b1nd.dodamcore.wakeupsong.application.dto.res.ChartRes;
import b1nd.dodaminfra.webclient.WebClientSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class MelonClient implements MusicChartClient {

    private final MelonProperties melonProperties;
    private final WebClientSupport webClientSupport;

    @Override
    public CompletableFuture<List<ChartRes>> getList() {
        return webClientSupport.get(melonProperties.getUrl(), String.class)
                .flatMap(html -> Mono.fromCallable(() -> MelonChartParser.parse(html)))
                        .toFuture();
    }
}
