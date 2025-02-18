package b1nd.dodam.domain.rds.club.service;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.exception.ClubDuplicateException;
import b1nd.dodam.domain.rds.club.exception.ClubNotFoundException;
import b1nd.dodam.domain.rds.club.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

    public Club findById(UUID id) {
        return clubRepository.findById(id)
                .orElseThrow(ClubNotFoundException::new);
    }

    public void deleteClub(Club club) {
        clubRepository.delete(club);
    }
}
