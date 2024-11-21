package b1nd.dodam.domain.rds.division.service;

import b1nd.dodam.domain.rds.division.entity.Division;
import b1nd.dodam.domain.rds.division.exception.DivisionNotFoundException;
import b1nd.dodam.domain.rds.division.repository.DivisionRepository;
import b1nd.dodam.domain.rds.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public void delete(Long id) {
        divisionRepository.deleteById(id);
    }


    public List<Division> getByMember(Member member, Long lastId, int size) {
        return divisionRepository.findByMember(member, lastId, PageRequest.of(0, size));
    }

    public List<Division> getAll(Long lastId, int size) {
        return divisionRepository.findNextPageWithCursor(lastId, PageRequest.of(0, size));
    }
}
