package b1nd.dodam.domain.rds.point.service;

import b1nd.dodam.domain.rds.point.entity.PointReason;
import b1nd.dodam.domain.rds.point.enumeration.PointType;
import b1nd.dodam.domain.rds.point.exception.PointReasonNotFoundException;
import b1nd.dodam.domain.rds.point.repository.PointReasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointReasonService {

    private final PointReasonRepository repository;

    public void save(PointReason reason) {
        repository.save(reason);
    }

    public void delete(PointReason reason) {
        repository.delete(reason);
    }

    public boolean checkReasonDuplication(String reason) {
        return repository.existsByReason(reason);
    }

    public PointReason getBy(Integer id) {
        return repository.findById(id)
                .orElseThrow(PointReasonNotFoundException::new);
    }

    public List<PointReason> getBy(PointType type) {
        return repository.findByPointType(type);
    }

}
