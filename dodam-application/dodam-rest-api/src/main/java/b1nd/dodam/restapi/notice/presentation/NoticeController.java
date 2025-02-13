package b1nd.dodam.restapi.notice.presentation;

import b1nd.dodam.domain.rds.notice.enumration.NoticeStatus;
import b1nd.dodam.restapi.notice.application.NoticeUseCase;
import b1nd.dodam.restapi.notice.application.data.req.GenerateNoticeReq;
import b1nd.dodam.restapi.notice.application.data.res.NoticeRes;
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
            @RequestParam Long lastId,
            @RequestParam int limit,
            @RequestParam NoticeStatus status

    ){
        return noticeUseCase.getNotices(lastId, limit, status);
    }

    @GetMapping("/{id}/division")
    public ResponseData<List<NoticeRes>> getBy(
            @PathVariable Long id,
            @RequestParam Long lastId,
            @RequestParam int limit
    ){
        return noticeUseCase.getNoticesByDivision(id, lastId, limit);
    }

    //TODO 페이징 적용
    @GetMapping("/{keyword}")
    public ResponseData<List<NoticeRes>> searchByKeyword(@PathVariable String keyword){
        return noticeUseCase.searchNotices(keyword);
    }

}
