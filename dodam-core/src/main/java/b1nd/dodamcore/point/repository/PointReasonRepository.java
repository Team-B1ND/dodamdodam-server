package b1nd.dodamcore.point.repository;

import b1nd.dodamcore.point.domain.entity.PointReason;
import b1nd.dodamcore.point.domain.enums.PointType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointReasonRepository extends JpaRepository<PointReason, Integer> {

    boolean existsByReason(String reason);

    List<PointReason> findByPointType(PointType type);

}
