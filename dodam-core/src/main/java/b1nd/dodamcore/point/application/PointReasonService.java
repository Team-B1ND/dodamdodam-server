package b1nd.dodamcore.point.application;

import b1nd.dodamcore.point.domain.entity.PointReason;
import b1nd.dodamcore.point.domain.enums.PointType;
import b1nd.dodamcore.point.domain.exception.PointReasonNotFoundException;
import b1nd.dodamcore.point.repository.PointReasonRepository;
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
