package b1nd.dodaminfra.webclient;

import b1nd.dodaminfra.webclient.exception.WebClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebClientSupport {

    private final WebClient webClient;

    public <T> ResponseEntity<T> get(String url, Class<T> responseDtoClass) {
        return webClient.method(HttpMethod.GET)
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::isError, onError())
                .toEntity(responseDtoClass)
                .block();
    }

    public <T, V> ResponseEntity<T> post(String url, V requestDto, Class<T> responseDtoClass) {
        return webClient.method(HttpMethod.POST)
                .uri(url)
                .bodyValue(requestDto)
                .retrieve()
                .onStatus(HttpStatusCode::isError, onError())
                .toEntity(responseDtoClass)
                .block();
    }

    private Function<ClientResponse, Mono<? extends Throwable>> onError() {
        return response -> {
            log.error("ClientException Status : " + response.statusCode());
            log.error("ClientException Body : " + response.bodyToMono(Object.class).block());

            return Mono.error(WebClientException::new);
        };
    }

}