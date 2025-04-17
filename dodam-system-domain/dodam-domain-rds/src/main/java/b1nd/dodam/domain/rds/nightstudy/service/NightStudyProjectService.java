package b1nd.dodam.domain.rds.nightstudy.service;

import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProject;
import b1nd.dodam.domain.rds.nightstudy.exception.NightStudyNotFoundException;
import b1nd.dodam.domain.rds.nightstudy.repository.NightStudyProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NightStudyProjectService {

    private final NightStudyProjectRepository repository;

    public void save(NightStudyProject nightStudyProject) {
        repository.save(nightStudyProject);
    }

    public void delete(NightStudyProject nightStudyProject) {
        repository.delete(nightStudyProject);
    }

    public NightStudyProject getBy(Long id) {
        return repository.findById(id)
                .orElseThrow(NightStudyNotFoundException::new);
    }

    public boolean checkDurationDuplication(List<Integer>, LocalDate startAt, LocalDate endAt) {
        return repository.existsValidByStudentAndDate
    }
}
