package b1nd.dodamapi.conference;

import b1nd.dodamapi.common.response.ResponseData;
import b1nd.dodamcore.conference.application.ConferenceService;
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

    private final ConferenceService conferenceService;

    @GetMapping
    public CompletableFuture<ResponseData<List<ConferenceRes>>> get() {
        return conferenceService.get()
                .thenApply(res -> ResponseData.ok("컨퍼런스 조회 성공", res));
    }

}