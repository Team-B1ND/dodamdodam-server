package b1nd.dodam.domain.rds.club.repository;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.club.enumeration.ClubType;
import b1nd.dodam.domain.rds.club.exception.ClubNotFoundException;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.List;

public interface ClubRepository extends JpaRepository<Club, Long> {
    default Club getByClubId(long id) {
        return findByIdAndStateNot(id, ClubStatus.DELETED).orElseThrow(ClubNotFoundException::new);
    }

    default Club getByClubIdWithLock(long clubId) {
        return findByIdWithLock(clubId).orElseThrow(ClubNotFoundException::new);
    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM club c WHERE c.id = :id")
    Optional<Club> findByIdWithLock(Long id);

    @EntityGraph(attributePaths = {"teacher", "teacher.member"})
    List<Club> findAllByStateNot(ClubStatus state);

    @EntityGraph(attributePaths = {"teacher", "teacher.member"})
    Optional<Club> findByIdAndStateNot(Long id, ClubStatus state);

    boolean existsByName(String name);

    Optional<Club> findByIdAndState(Long id, ClubStatus state);

    List<Club> findByIdIn(List<Long> ids);

    Optional<List<Club>> findByTypeAndState(ClubType clubType,  ClubStatus clubStatus);
}
