package b1nd.dodam.restapi.banner.presentation;

import b1nd.dodam.domain.rds.banner.entity.Banner;
import b1nd.dodam.restapi.banner.application.BannerUseCase;
import b1nd.dodam.restapi.banner.application.data.req.BannerReq;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/banner")
@RequiredArgsConstructor
public class BannerController {

    private final BannerUseCase useCase;

    @PostMapping
    public Response create(@RequestBody @Valid BannerReq req) {
        return useCase.create(req);
    }

    @PatchMapping("/{id}")
    public Response modify(@PathVariable int id, @RequestBody BannerReq req) {
        return useCase.modify(id, req);
    }

    @DeleteMapping("/{id}")
    public Response deleteById(@PathVariable int id) {
        return useCase.deleteById(id);
    }

    @PatchMapping("/activate/{id}")
    public Response activate(@PathVariable int id) {
        return useCase.activate(id);
    }

    @PatchMapping("/deactivate/{id}")
    public Response deactivateBanner(@PathVariable int id) {
        return useCase.deactivate(id);
    }

    @GetMapping("/{id}")
    public ResponseData<Banner> getById(@PathVariable int id) {
        return useCase.getById(id);
    }

    @GetMapping("/active")
    public ResponseData<List<Banner>> getActivates() {
        return useCase.getActivates();
    }

    @GetMapping
    public ResponseData<List<Banner>> getAllOrderByIdDesc() {
        return useCase.getAllOrderByIdDesc();
    }

}
