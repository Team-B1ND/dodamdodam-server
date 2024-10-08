package b1nd.dodam.domain.rds.point.repository;

import b1nd.dodam.domain.rds.point.entity.PointReason;
import b1nd.dodam.domain.rds.point.enumeration.PointType;
import b1nd.dodam.domain.rds.point.exception.PointNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointReasonRepository extends JpaRepository<PointReason, Integer> {

    boolean existsByReason(String reason);

    List<PointReason> findByPointType(PointType type);

    default PointReason getById(int id) {
        return findById(id)
                .orElseThrow(PointNotFoundException::new);
    }

}
