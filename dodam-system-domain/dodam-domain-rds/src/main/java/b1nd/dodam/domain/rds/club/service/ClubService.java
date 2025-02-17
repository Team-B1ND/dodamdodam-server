package b1nd.dodam.domain.rds.club.service;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.exception.ClubDuplicateException;
import b1nd.dodam.domain.rds.club.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;

    public void checkIsNameDuplicated(String name){
        if(clubRepository.existsByName(name)){
            throw new ClubDuplicateException();
        }
    }

    public void save(Club club) {
        clubRepository.save(club);
    }
}
