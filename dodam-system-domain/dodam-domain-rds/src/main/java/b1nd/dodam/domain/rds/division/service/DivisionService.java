package b1nd.dodam.domain.rds.division.service;

import b1nd.dodam.domain.rds.division.entity.Division;
import b1nd.dodam.domain.rds.division.exception.DivisionDuplicateException;
import b1nd.dodam.domain.rds.division.exception.DivisionNotFoundException;
import b1nd.dodam.domain.rds.division.repository.DivisionRepository;
import b1nd.dodam.domain.rds.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DivisionService {
    private final DivisionRepository divisionRepository;

    public void save(Division division) {
        divisionRepository.save(division);
    }

    public Division getById(Long id) {
        return divisionRepository.findById(id)
                .orElseThrow(DivisionNotFoundException::new);
    }

    public List<Division> getAllByIds(List<Long> ids){
        return divisionRepository.findAllById(ids);
    }

    public void checkIsNotDuplicateName(String name) {
        if (divisionRepository.findByName(name).isPresent()) {
            throw new DivisionDuplicateException();
        }
    }

    public void delete(Long id) {
        divisionRepository.deleteById(id);
    }


    public List<Division> getByMember(Member member, Long lastId, int limit) {
        return divisionRepository.findByMember(member, lastId, PageRequest.of(0, limit));
    }

    public List<Division> getAll(Long lastId, int limit) {
        return divisionRepository.findNextPageWithCursor(lastId, PageRequest.of(0, limit));
    }
}
