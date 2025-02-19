package b1nd.dodam.domain.rds.club.service;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.club.exception.ClubDuplicateException;
import b1nd.dodam.domain.rds.club.exception.ClubNotFoundException;
import b1nd.dodam.domain.rds.club.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;
    private final String deleted =  "_deleted";

    public void checkIsNameDuplicated(String name){
        if(clubRepository.existsByName(name)){
            throw new ClubDuplicateException();
        }
    }

    public void update(Club club, String name, String subject, String shortDescription, String description) {
        club.updateInfo(name, subject, shortDescription, description);
        clubRepository.save(club);
    }

    public void save(Club club) {
        clubRepository.save(club);
    }

    public Club findById(Long id) {
        return clubRepository.findById(id)
                .orElseThrow(ClubNotFoundException::new);
    }

    public void deleteClub(Club club) {
        club.updateStatus(club.getName() + deleted, ClubStatus.DELETED);
        clubRepository.save(club);
    }
}
