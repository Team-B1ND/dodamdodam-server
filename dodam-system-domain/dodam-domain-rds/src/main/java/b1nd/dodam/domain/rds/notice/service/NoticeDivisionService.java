package b1nd.dodam.domain.rds.notice.service;

import b1nd.dodam.domain.rds.notice.entity.NoticeDivision;
import b1nd.dodam.domain.rds.notice.exception.NoticeDivisionNotFoundException;
import b1nd.dodam.domain.rds.notice.repository.NoticeDivisionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeDivisionService {

    private final NoticeDivisionRepository noticeDivisionRepository;

    public void save(NoticeDivision noticeDivision){
        noticeDivisionRepository.save(noticeDivision);
    }

    public NoticeDivision getById(Long id){
        return noticeDivisionRepository.findById(id)
                .orElseThrow(NoticeDivisionNotFoundException::new);
    }

    public void saveAll(List<NoticeDivision> noticeDivisions){
        noticeDivisionRepository.saveAll(noticeDivisions);
    }
}
