package b1nd.dodamapi.conference;

import b1nd.dodamapi.common.response.ResponseData;
import b1nd.dodamcore.conference.application.ConferenceService;
import b1nd.dodamcore.conference.application.dto.res.ConferenceRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/conference")
@RequiredArgsConstructor
public class ConferenceController {

    private final ConferenceService conferenceService;

    @GetMapping
    public ResponseData<List<ConferenceRes>> get() {
        return ResponseData.ok("컨퍼런스 조회 성공", conferenceService.get().join());
    }

}