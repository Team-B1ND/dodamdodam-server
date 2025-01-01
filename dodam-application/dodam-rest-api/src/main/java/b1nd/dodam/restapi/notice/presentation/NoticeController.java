package b1nd.dodam.restapi.notice.presentation;

import b1nd.dodam.restapi.notice.application.NoticeDivisionUseCase;
import b1nd.dodam.restapi.notice.application.NoticeUseCase;
import b1nd.dodam.restapi.notice.application.data.req.AddNoticeDivisionReq;
import b1nd.dodam.restapi.notice.application.data.req.GenerateNoticeReq;
import b1nd.dodam.restapi.notice.application.data.res.NoticeRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeUseCase noticeUseCase;
    private final NoticeDivisionUseCase noticeDivisionUseCase;

    @PostMapping
    public ResponseData<Long> generate(@RequestBody GenerateNoticeReq generateNoticeReq){
        return noticeUseCase.register(generateNoticeReq);
    }

    @GetMapping
    public ResponseData<List<NoticeRes>> getCreated(){
        return noticeUseCase.getCreated();
    }

    @PatchMapping("/division/{id}")
    public Response addStatus(
            @PathVariable Long id,
            @RequestBody AddNoticeDivisionReq addNoticeDivisionReq){
        return noticeDivisionUseCase.addDivision(id, addNoticeDivisionReq);
    }

}
