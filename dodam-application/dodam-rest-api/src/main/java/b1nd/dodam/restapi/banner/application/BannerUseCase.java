package b1nd.dodam.restapi.banner.application;

import b1nd.dodam.domain.rds.banner.entity.Banner;
import b1nd.dodam.domain.rds.banner.enumeration.BannerStatus;
import b1nd.dodam.domain.rds.banner.repository.BannerRepository;
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

    private final BannerRepository repository;

    public Response create(BannerReq req) {
        repository.save(req.mapToBanner());
        return Response.created("배너 생성 성공");
    }

    public Response modify(int id, BannerReq req) {
        Banner banner = repository.getById(id);
        banner.updateBanner(req.title(), req.image(), req.url(), req.expireAt());
        return Response.noContent("배너 수정 성공");
    }

    public Response activate(int id) {
        Banner banner = repository.getById(id);
        banner.activateStatus();
        return Response.noContent("배너 활성화 성공");
    }

    public Response deactivate(int id) {
        Banner banner = repository.getById(id);
        banner.deactivateStatus();
        return Response.noContent("배너 비활성화 성공");
    }

    public Response deleteById(int id) {
        repository.deleteById(id);
        return Response.noContent("배너 삭제 성공");
    }

    @Transactional(readOnly = true)
    public ResponseData<Banner> getById(int id) {
        return ResponseData.ok("배너 단일 조회 성공", repository.getById(id));
    }

    @Transactional(readOnly = true)
    public ResponseData<List<Banner>> getActivates() {
        return ResponseData.ok("활성화 배너 조회 성공", repository.findByStatusOrderByIdDesc(BannerStatus.ACTIVE));
    }

    @Transactional(readOnly = true)
    public ResponseData<List<Banner>> getAllOrderByIdDesc() {
        return ResponseData.ok("배너 전체 조회 성공", repository.findAllByOrderByIdDesc());
    }

}
