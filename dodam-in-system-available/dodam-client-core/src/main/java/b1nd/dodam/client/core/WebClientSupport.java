package b1nd.dodam.client.core;

import b1nd.dodam.client.core.exception.WebClientException;
import b1nd.dodam.core.exception.CustomException;
import b1nd.dodam.core.exception.global.GlobalExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class WebClientSupport {

    private final WebClient webClient;

    public <T> Mono<T> get(String url, Class<T> responseDtoClass, String... headers) {
        return webClient.get()
                .uri(url)
                .headers(convertStringToHttpHeaders(headers))
                .retrieve()
                .onStatus(HttpStatusCode::isError, onError())
                .bodyToMono(responseDtoClass);
    }

    public <V> void post(String url, V body, String... headers) {
        webClient.post()
                .uri(url)
                .headers(convertStringToHttpHeaders(headers))
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, onError())
                .toBodilessEntity()
                .subscribe();
    }

    public <T, V> Mono<T> post(String url, V body, Class<T> responseClass, String... headers) {
        return webClient.post()
                .uri(url)
                .headers(convertStringToHttpHeaders(headers))
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    System.out.println("S400");
                    clientResponse.bodyToMono(String.class).subscribe(e -> {
                        System.out.println(e);
                    });
                    return  Mono.error(new RuntimeException("오류발생: " + clientResponse.statusCode()));
                })
                .onStatus(HttpStatusCode::isError, onError())
                .bodyToMono(responseClass);
    }

    private Function<ClientResponse, Mono<? extends Throwable>> onError() {
        return response -> {
            System.out.println("onError - ");
            System.out.println(response.bodyToMono(String.class).block());
            throw new WebClientException(response.statusCode().value());
        };
    }

    private Consumer<HttpHeaders> convertStringToHttpHeaders(String... headers) {
        if(headers.length % 2 != 0) {
            throw new CustomException(GlobalExceptionCode.INTERNAL_SERVER);
        }

        return httpHeaders -> {
            for(int i = 0; i<headers.length; i+=2) {
                httpHeaders.add(headers[i], headers[i+1]);
            }
        };
    }

}
