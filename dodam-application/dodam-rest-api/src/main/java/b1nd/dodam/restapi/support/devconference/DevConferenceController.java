package b1nd.dodam.restapi.support.devconference;

import b1nd.dodam.codenary.client.CodenaryClient;
import b1nd.dodam.codenary.client.data.res.ConferenceRes;
import b1nd.dodam.restapi.support.data.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/conference")
@RequiredArgsConstructor
public class DevConferenceController {

    private final CodenaryClient codenaryClient;

    @Async
    @GetMapping
    public CompletableFuture<ResponseData<List<ConferenceRes>>> getByMonth() {
        return codenaryClient.getByMonth()
                .thenApply(data -> ResponseData.ok("컨퍼런스 조회 성공", data));
    }

}
