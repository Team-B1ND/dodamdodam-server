package b1nd.dodaminfra.api.neis;

import b1nd.dodamcore.meal.application.MealClient;
import b1nd.dodaminfra.webclient.WebClientSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class NeisClient implements MealClient {

    private final NeisProperties neisProperties;
    private final WebClientSupport webClientSupport;

    @Override
    public String getMeal(String date) {
        return webClientSupport.get(
                UriComponentsBuilder.fromUriString(neisProperties.getUrl())
                        .build(neisProperties.getApiKey(), date).toString(),
                String.class
        ).block();
    }
}
