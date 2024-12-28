package b1nd.dodam.restapi.notice.presentation;

import b1nd.dodam.restapi.notice.application.NoticeUseCase;
import b1nd.dodam.restapi.notice.application.data.req.GenerateNoticeReq;
import b1nd.dodam.restapi.support.data.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeUseCase noticeUseCase;

    @PostMapping
    public Response generate(@RequestBody GenerateNoticeReq generateNoticeReq){
        return noticeUseCase.register(generateNoticeReq);
    }

}
