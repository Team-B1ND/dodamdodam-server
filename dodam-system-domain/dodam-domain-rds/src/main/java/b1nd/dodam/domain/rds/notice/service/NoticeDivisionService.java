package b1nd.dodam.domain.rds.notice.service;

import b1nd.dodam.domain.rds.division.entity.Division;
import b1nd.dodam.domain.rds.notice.entity.Notice;
import b1nd.dodam.domain.rds.notice.entity.NoticeDivision;
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

    public List<NoticeDivision> getAllByDivision(Division division){
        return noticeDivisionRepository.findAllByDivision(division);
    }

    public List<NoticeDivision> getAllByNotice(Notice notice){
        return noticeDivisionRepository.findAllByNotice(notice);
    }

}
