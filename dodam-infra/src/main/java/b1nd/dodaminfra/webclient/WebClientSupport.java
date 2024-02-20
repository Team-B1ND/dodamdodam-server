package b1nd.dodaminfra.webclient;

import b1nd.dodaminfra.webclient.exception.WebClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Function;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebClientSupport {

    private final WebClient webClient;

    public <T> Mono<T> get(String url, Class<T> responseDtoClass, String... headers) {
        return webClient.method(HttpMethod.GET)
                .uri(url)
                .headers(convertStringToHttpHeaders(headers))
                .retrieve()
                .onStatus(HttpStatusCode::isError, onError())
                .bodyToMono(responseDtoClass);
    }

    public <T, V> Mono<T> post(String url, V requestDto, Class<T> responseDtoClass, String... headers) {
        return webClient.method(HttpMethod.POST)
                .uri(url)
                .headers(convertStringToHttpHeaders(headers))
                .bodyValue(requestDto)
                .retrieve()
                .onStatus(HttpStatusCode::isError, onError())
                .bodyToMono(responseDtoClass);
    }

    private Function<ClientResponse, Mono<? extends Throwable>> onError() {
        return response -> {
            log.error("WebClient Error Status : " + response.statusCode());
            log.error("WebClient Error Body : " + response.bodyToMono(Object.class).block());

            return Mono.error(WebClientException::new);
        };
    }

    private Consumer<HttpHeaders> convertStringToHttpHeaders(String... headers) {
        return httpHeaders -> {
            for(int i = 0; i<headers.length; i+=2) {
                httpHeaders.add(headers[i], headers[i+1]);
            }
        };
    }

}