package b1nd.dodam.domain.rds.banner.repository;

import b1nd.dodam.domain.rds.banner.entity.Banner;
import b1nd.dodam.domain.rds.banner.enumeration.BannerStatus;
import b1nd.dodam.domain.rds.banner.exception.BannerNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Integer> {

    List<Banner> findAllByOrderByIdDesc();

    List<Banner> findByStatusOrderByIdDesc(BannerStatus bannerStatus);

    default Banner getById(int id) {
        return findById(id)
                .orElseThrow(BannerNotFoundException::new);
    }

}
