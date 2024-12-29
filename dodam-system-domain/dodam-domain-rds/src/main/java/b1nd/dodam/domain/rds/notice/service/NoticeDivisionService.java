package b1nd.dodam.domain.rds.notice.service;

import b1nd.dodam.domain.rds.notice.entity.NoticeDivision;
import b1nd.dodam.domain.rds.notice.repository.NoticeDivisionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeDivisionService {

    private final NoticeDivisionRepository noticeDivisionRepository;

    public void save(NoticeDivision noticeDivision){
        noticeDivisionRepository.save(noticeDivision);
    }

}
