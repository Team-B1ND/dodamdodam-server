package b1nd.dodamapi.banner;

import b1nd.dodamcore.common.response.Response;
import b1nd.dodamcore.common.response.ResponseData;
import b1nd.dodamcore.banner.application.BannerService;
import b1nd.dodamcore.banner.application.dto.req.BannerReq;
import b1nd.dodamcore.banner.domain.entity.Banner;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/banner")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @PostMapping
    public Response createBanner(@RequestBody @Valid BannerReq createBannerDto) {
        bannerService.createBanner(createBannerDto);
        return Response.created("배너 생성 성공");
    }

    @PatchMapping("/{id}")
    public Response modifyBanner(
            @PathVariable int id,
            @RequestBody BannerReq modifyBannerDto
    ) {
        bannerService.modifyBanner(id, modifyBannerDto);
        return Response.ok("배너 수정 성공");
    }

    @DeleteMapping("/{id}")
    public Response deleteBanner(@PathVariable int id) {
        bannerService.deleteBanner(id);
        return Response.ok("배너 삭제 성공");
    }

    @PatchMapping("/activate/{id}")
    public Response activateBanner(@PathVariable int id) {
        bannerService.activateBanner(id);
        return Response.ok("배너 유효 설정 성공");
    }

    @PatchMapping("/deactivate/{id}")
    public Response deactivateBanner(@PathVariable int id) {
        bannerService.deactivateBanner(id);
        return Response.ok("배너 유효 취소 설정 성공");
    }

    @GetMapping
    public ResponseData<List<Banner>> getBanners() {
        List<Banner> bannerList = bannerService.getBanners();
        return ResponseData.ok("배너 전체 조회 성공", bannerList);
    }

    @GetMapping("/active")
    public ResponseData<List<Banner>> getActiveBanner() {
        List<Banner> bannerList = bannerService.getActivateBanners();
        return ResponseData.ok("유효 배너 조회 성공", bannerList);
    }

    @GetMapping("/{id}")
    public ResponseData<Banner> getBannerById(@PathVariable int id) {
        Banner banner = bannerService.getBannerById(id);
        return ResponseData.ok("특정 배너 조회 성공", banner);
    }
}
