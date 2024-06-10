package b1nd.dodamapi.conference.handler;

import b1nd.dodamcore.common.response.ResponseData;
import b1nd.dodamapi.conference.usecase.ConferenceUseCase;
import b1nd.dodamcore.conference.application.dto.res.ConferenceRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/conference")
@RequiredArgsConstructor
public class ConferenceController {

    private final ConferenceUseCase useCase;

    @GetMapping
    public CompletableFuture<ResponseData<List<ConferenceRes>>> getByMonth() {
        return useCase.getByMonth();
    }

}
