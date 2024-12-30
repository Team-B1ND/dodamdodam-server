package b1nd.dodam.restapi.notice.presentation;

import b1nd.dodam.restapi.notice.application.NoticeDivisionUseCase;
import b1nd.dodam.restapi.notice.application.NoticeUseCase;
import b1nd.dodam.restapi.notice.application.data.req.AddNoticeDivisionReq;
import b1nd.dodam.restapi.notice.application.data.req.GenerateNoticeReq;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeUseCase noticeUseCase;
    private final NoticeDivisionUseCase noticeDivisionUseCase;

    @PostMapping
    public ResponseData generate(@RequestBody GenerateNoticeReq generateNoticeReq){
        return noticeUseCase.register(generateNoticeReq);
    }

    @PatchMapping("/division/{id}")
    public Response addStatus(
            @PathVariable Long id,
            @RequestBody AddNoticeDivisionReq addNoticeDivisionReq){
        return noticeDivisionUseCase.addDivision(id, addNoticeDivisionReq);
    }

}
