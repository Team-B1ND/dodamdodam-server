package b1nd.dodaminfra.webclient;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class WebClientSupport {

    private final WebClient webClient;

    public <T> ResponseEntity<T> get(String url, Class<T> responseDtoClass) {
        return webClient.method(HttpMethod.GET)
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, onClientError())
                .onStatus(HttpStatusCode::is5xxServerError, onServerError())
                .toEntity(responseDtoClass)
                .block();
    }

    public <T, V> ResponseEntity<T> post(String url, V requestDto, Class<T> responseDtoClass) {
        return webClient.method(HttpMethod.POST)
                .uri(url)
                .bodyValue(requestDto)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, onClientError())
                .onStatus(HttpStatusCode::is5xxServerError, onServerError())
                .toEntity(responseDtoClass)
                .block();
    }

    private Function<ClientResponse, Mono<? extends Throwable>> onClientError() {
        return response -> switch ((HttpStatus) response.statusCode()) {
            case BAD_REQUEST ->
                //return Mono.error(WebClientException.EXCEPTION);
                    Mono.error(new RuntimeException());
            case UNAUTHORIZED ->
                //return Mono.error(InvalidTokenException.EXCEPTION);
                    Mono.error(new RuntimeException());
            case GONE ->
                //return Mono.error(ExpiredTokenException.EXCEPTION);
                    Mono.error(new RuntimeException());
            default ->
                //return Mono.error(InternalServerException.EXCEPTION);
                    Mono.error(new RuntimeException());
        };
    }

    private Function<ClientResponse, Mono<? extends Throwable>> onServerError() {
        return response -> Mono.error(new RuntimeException());
    }

}