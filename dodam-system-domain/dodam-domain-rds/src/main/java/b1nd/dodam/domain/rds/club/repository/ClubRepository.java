package b1nd.dodam.domain.rds.club.repository;

import b1nd.dodam.domain.rds.club.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClubRepository extends JpaRepository<Club, UUID> {
    boolean existsByName(String name);
}
