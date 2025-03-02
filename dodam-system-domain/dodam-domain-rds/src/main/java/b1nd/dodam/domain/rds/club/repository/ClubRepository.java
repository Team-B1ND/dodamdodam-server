package b1nd.dodam.domain.rds.club.repository;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.club.exception.ClubNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface ClubRepository extends JpaRepository<Club, Long> {
    default Club getByClubId(long id) {
        return findById(id).orElseThrow(ClubNotFoundException::new);
    }

    default Club getByClubIdAndState(long clubId, ClubStatus clubStatus) {
        return findByIdAndState(clubId, clubStatus).orElseThrow(ClubNotFoundException::new);
    }

    boolean existsByName(String name);

    Optional<Club> findByIdAndState(Long id, ClubStatus state);

    List<Club> findByIdIn(List<Long> ids);
}
