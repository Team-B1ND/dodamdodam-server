package b1nd.dodam.domain.rds.club.repository;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.club.exception.ClubNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Long> {
    default Club getByClubId(long id) {
        return findByIdAndStateNot(id, ClubStatus.DELETED).orElseThrow(ClubNotFoundException::new);
    }

    default Club getByClubIdAndState(long clubId, ClubStatus clubStatus) {
        return findByIdAndState(clubId, clubStatus).orElseThrow(ClubNotFoundException::new);
    }

    List<Club> findAllByStateNot(ClubStatus state);

    Optional<Club> findByIdAndStateNot(Long id, ClubStatus state);

    boolean existsByName(String name);

    Optional<Club> findByIdAndState(Long id, ClubStatus state);
}
