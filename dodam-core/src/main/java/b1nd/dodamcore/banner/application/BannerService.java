package b1nd.dodamcore.banner.application;

import b1nd.dodamcore.banner.application.dto.req.BannerReq;
import b1nd.dodamcore.banner.domain.entity.Banner;
import b1nd.dodamcore.banner.domain.enums.BannerStatus;
import b1nd.dodamcore.banner.domain.exception.BannerNotFoundException;
import b1nd.dodamcore.banner.repository.BannerRepository;
import b1nd.dodamcore.common.util.ModifyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BannerService {

    private final BannerRepository bannerRepository;

    @Transactional(rollbackFor = Exception.class)
    public void createBanner(BannerReq createBannerReq) {
        Banner banner = createBannerReq.mapToBanner();
        bannerRepository.save(banner);
    }

    @Transactional(rollbackFor = Exception.class)
    public void modifyBanner(int id, BannerReq modifyBannerReq) {

        Banner banner = bannerRepository.findById(id)
                .orElseThrow(BannerNotFoundException::new);

        banner.updateBanner(
                ModifyUtil.modifyIfNotNull(modifyBannerReq.title(), banner.getTitle()),
                ModifyUtil.modifyIfNotNull(modifyBannerReq.image(), banner.getImageUrl()),
                ModifyUtil.modifyIfNotNull(modifyBannerReq.url(), banner.getRedirectUrl()),
                ModifyUtil.modifyIfNotNull(modifyBannerReq.expireAt(), banner.getExpireAt())
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteBanner(int id) {

        Banner banner = bannerRepository.findById(id)
                .orElseThrow(BannerNotFoundException::new);

        bannerRepository.delete(banner);
    }

    @Transactional(rollbackFor = Exception.class)
    public void activateBanner(int id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(BannerNotFoundException::new);
        banner.activateStatus();
    }

    @Transactional(rollbackFor = Exception.class)
    public void deactivateBanner(int id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(BannerNotFoundException::new);
        banner.deactivateStatus();
    }

    public List<Banner> getBanners() {
        return bannerRepository.findAllByOrderByIdDesc();
    }

    public Banner getBannerById(int id) {
        return bannerRepository.findById(id)
                .orElseThrow(BannerNotFoundException::new);
    }

    public List<Banner> getActivateBanners() {
        return bannerRepository.findByStatusOrderByIdDesc(BannerStatus.ACTIVE);
    }
}
