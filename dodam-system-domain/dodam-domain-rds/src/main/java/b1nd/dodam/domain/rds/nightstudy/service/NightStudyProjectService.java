package b1nd.dodam.domain.rds.nightstudy.service;

import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProject;
import b1nd.dodam.domain.rds.nightstudy.exception.NightStudyNotFoundException;
import b1nd.dodam.domain.rds.nightstudy.repository.NightStudyProjectRepository;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NightStudyProjectService {

    private final NightStudyProjectRepository repository;

    public NightStudyProject save(NightStudyProject nightStudyProject) {
        return repository.save(nightStudyProject);
    }

    public void delete(NightStudyProject nightStudyProject) {
        repository.delete(nightStudyProject);
    }

    public NightStudyProject getById(Long id) {
        return repository.findById(id)
                .orElseThrow(NightStudyNotFoundException::new);
    }

    public List<NightStudyProject> getAllByDateRange(LocalDate date) {
    return repository.findByStartAtLessThanEqualAndEndAtGreaterThanEqual(date, date);
    }

    public List<NightStudyProject> getAllRoomsWithProjects(LocalDate date) {
       return repository.findByStatusNotAndStartAtLessThanEqualAndEndAtGreaterThanEqualOrderByRoom(ApprovalStatus.REJECTED, date, date);
    }
}
