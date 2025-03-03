package b1nd.dodam.domain.rds.club.repository;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.club.exception.ClubNotFoundException;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;
import java.util.List;

public interface ClubRepository extends JpaRepository<Club, Long> {
    default Club getByClubId(long id) {
        return findById(id).orElseThrow(ClubNotFoundException::new);
    }

    default Club getByClubIdWithLock(long clubId) {
        return findAllById(clubId).orElseThrow(ClubNotFoundException::new);
    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Club> findAllById(Long id);

    boolean existsByName(String name);

    Optional<Club> findByIdAndState(Long id, ClubStatus state);

    List<Club> findByIdIn(List<Long> ids);
}
