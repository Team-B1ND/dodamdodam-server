package b1nd.dodam.melon.chart.client;

import b1nd.dodam.client.core.WebClientSupport;
import b1nd.dodam.melon.chart.client.data.res.ChartRes;
import b1nd.dodam.melon.chart.client.properties.MelonProperties;
import b1nd.dodam.melon.chart.client.support.MelonChartParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class MelonChartClient {

    private final MelonProperties melonProperties;
    private final WebClientSupport webClientSupport;

    public CompletableFuture<List<ChartRes>> getList() {
        return webClientSupport.get(melonProperties.getUrl(), String.class)
                .flatMap(html -> Mono.fromCallable(() -> MelonChartParser.parse(html)))
                        .toFuture();
    }

}
