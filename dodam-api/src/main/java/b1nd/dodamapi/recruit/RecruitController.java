package b1nd.dodamapi.recruit;

import b1nd.dodamapi.common.response.Response;
import b1nd.dodamapi.common.response.ResponseData;
import b1nd.dodamcore.recruit.application.RecruitService;
import b1nd.dodamcore.recruit.application.dto.req.RecruitReq;
import b1nd.dodamcore.recruit.application.dto.res.RecruitPageRes;
import b1nd.dodamcore.recruit.application.dto.res.RecruitRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recruit")
@RequiredArgsConstructor
public class RecruitController {

    private final RecruitService recruitService;

    @GetMapping
    public ResponseData<RecruitPageRes> getRecruits(@RequestParam(name = "page") int page) {
        RecruitPageRes recruitList = recruitService.getRecruitsByPaging(page);
        return ResponseData.ok("채용 의뢰서 조회 성공", recruitList);
    }

    @GetMapping("/{id}")
    public ResponseData<RecruitRes> getRecruitById(@PathVariable int id) {
        RecruitRes recruit = recruitService.getRecruitById(id);
        return ResponseData.ok("채용 의뢰서 단일 조회 성공", recruit);
    }

    @PostMapping
    public Response createRecruit(@RequestBody @Valid RecruitReq createRecruitReq) {
        recruitService.createRecruit(createRecruitReq);
        return Response.created("채용 의뢰서 작성 성공");
    }

    @PatchMapping("/{id}")
    public Response modifyRecruit(
            @PathVariable int id,
            @RequestBody RecruitReq modifyRecruitReq
    ) {
        recruitService.modifyRecruit(id, modifyRecruitReq);
        return Response.ok("채용 의뢰서 수정 성공");
    }

    @DeleteMapping("/{id}")
    public Response deleteRecruit(@PathVariable int id) {
        recruitService.deleteRecruit(id);
        return Response.ok("채용 의뢰서 삭제 성공");
    }
}
