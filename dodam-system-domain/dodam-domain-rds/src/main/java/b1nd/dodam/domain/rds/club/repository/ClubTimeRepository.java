package b1nd.dodam.domain.rds.club.repository;

import b1nd.dodam.domain.rds.club.entity.ClubTime;
import b1nd.dodam.domain.rds.club.enumeration.ClubTimeType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubTimeRepository extends JpaRepository<ClubTime, ClubTimeType> {
}
