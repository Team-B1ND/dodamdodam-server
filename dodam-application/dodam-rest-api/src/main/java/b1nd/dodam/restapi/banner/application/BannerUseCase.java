package b1nd.dodam.restapi.banner.application;

import b1nd.dodam.domain.rds.banner.entity.Banner;
import b1nd.dodam.domain.rds.banner.service.BannerService;
import b1nd.dodam.restapi.banner.application.data.req.BannerReq;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class BannerUseCase {

    private final BannerService service;

    public Response create(BannerReq req) {
        service.save(req.mapToBanner());
        return Response.created("배너 생성 성공");
    }

    public Response modify(int id, BannerReq req) {
        Banner banner = service.getById(id);
        banner.updateBanner(req.title(), req.image(), req.url(), req.expireAt());
        return Response.noContent("배너 수정 성공");
    }

    public Response activate(int id) {
        Banner banner = service.getById(id);
        banner.activateStatus();
        return Response.noContent("배너 활성화 성공");
    }

    public Response deactivate(int id) {
        Banner banner = service.getById(id);
        banner.deactivateStatus();
        return Response.noContent("배너 비활성화 성공");
    }

    public Response deleteById(int id) {
        service.deleteById(id);
        return Response.noContent("배너 삭제 성공");
    }

    @Transactional(readOnly = true)
    public ResponseData<Banner> getById(int id) {
        return ResponseData.ok("배너 단일 조회 성공", service.getById(id));
    }

    @Transactional(readOnly = true)
    public ResponseData<List<Banner>> getActivates() {
        return ResponseData.ok("활성화 배너 조회 성공", service.getActivates());
    }

    @Transactional(readOnly = true)
    public ResponseData<List<Banner>> getAllOrderByIdDesc() {
        return ResponseData.ok("배너 전체 조회 성공", service.getAllOrderByIdDesc());
    }

}
