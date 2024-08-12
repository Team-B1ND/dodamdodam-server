package b1nd.dodam.restapi.recruitment.presentation;

import b1nd.dodam.domain.rds.recruitment.service.data.RecruitPageRes;
import b1nd.dodam.restapi.recruitment.application.RecruitmentUseCase;
import b1nd.dodam.restapi.recruitment.application.data.req.RecruitReq;
import b1nd.dodam.restapi.recruitment.application.data.res.RecruitRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/recruit")
@RequiredArgsConstructor
public class RecruitmentController {

    private final RecruitmentUseCase useCase;

    @PostMapping
    public Response create(@RequestBody @Valid RecruitReq req) {
        return useCase.create(req);
    }

    @PatchMapping("/{id}")
    public Response modify(@PathVariable int id, @RequestBody RecruitReq req) {
        return useCase.modify(id, req);
    }

    @DeleteMapping("/{id}")
    public Response delete(@PathVariable int id) {
        return useCase.delete(id);
    }

    @GetMapping("/{id}")
    public ResponseData<RecruitRes> getById(@PathVariable int id) {
        return useCase.getById(id);
    }

    @GetMapping
    public ResponseData<RecruitPageRes> getAllOrderByIdDesc(@RequestParam(name = "page") int page,
                                                            @RequestParam(name = "size") int size) {
        return useCase.getAllOrderByIdDesc(page, size);
    }

}
