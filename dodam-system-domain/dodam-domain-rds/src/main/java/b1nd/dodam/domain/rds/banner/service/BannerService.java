package b1nd.dodam.domain.rds.banner.service;

import b1nd.dodam.domain.rds.banner.entity.Banner;
import b1nd.dodam.domain.rds.banner.enumeration.BannerStatus;
import b1nd.dodam.domain.rds.banner.exception.BannerNotFoundException;
import b1nd.dodam.domain.rds.banner.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BannerService {

    private final BannerRepository repository;

    public void save(Banner banner) {
        repository.save(banner);
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }

    public Banner getById(int id) {
        return repository.findById(id)
                .orElseThrow(BannerNotFoundException::new);
    }

    public List<Banner> getActivates() {
        return repository.findByStatusOrderByIdDesc(BannerStatus.ACTIVE);
    }

    public List<Banner> getAllOrderByIdDesc() {
        return repository.findAllByOrderByIdDesc();
    }

}
