package b1nd.dodamcore.banner.repository;

import b1nd.dodamcore.banner.domain.entity.Banner;
import b1nd.dodamcore.banner.domain.enums.BannerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Integer> {

    List<Banner> findAllByOrderByIdDesc();

    List<Banner> findByStatusOrderByIdDesc(BannerStatus bannerStatus);
}
