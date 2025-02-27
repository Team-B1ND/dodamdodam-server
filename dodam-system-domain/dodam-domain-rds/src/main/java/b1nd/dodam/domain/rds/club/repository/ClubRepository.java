package b1nd.dodam.domain.rds.club.repository;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.exception.ClubNotFoundException;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club, Long> {
    default Club getByClubId(long id) {
        return findById(id).orElseThrow(ClubNotFoundException::new);
    }
    boolean existsByName(String name);
}
