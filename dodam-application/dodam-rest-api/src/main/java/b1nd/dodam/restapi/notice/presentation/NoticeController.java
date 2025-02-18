package b1nd.dodam.restapi.notice.presentation;

import b1nd.dodam.restapi.notice.application.NoticeUseCase;
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

    @PostMapping
    public ResponseData<Long> generate(@RequestBody GenerateNoticeReq generateNoticeReq){
        return noticeUseCase.register(generateNoticeReq);
    }

    @GetMapping
    public ResponseData<List<NoticeRes>> getByStatus(
            @RequestParam String keyword,
            @RequestParam(required = false) Long lastId,
            @RequestParam int limit
    ){
        return noticeUseCase.getNotices(keyword, lastId, limit);
    }

    @GetMapping("/division")
    public ResponseData<List<NoticeRes>> getById(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) Long lastId,
            @RequestParam int limit
    ){
        return noticeUseCase.getNoticesByDivision(id, lastId, limit);
    }

    @DeleteMapping("/{id}")
    public Response deleteById(@PathVariable Long id){
        return noticeUseCase.deleteNotice(id);
    }

}
