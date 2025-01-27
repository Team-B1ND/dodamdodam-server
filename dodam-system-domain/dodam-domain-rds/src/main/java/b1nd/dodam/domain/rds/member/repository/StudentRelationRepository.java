package b1nd.dodam.domain.rds.member.repository;

import b1nd.dodam.domain.rds.member.entity.StudentRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRelationRepository extends JpaRepository<StudentRelation, Integer> {
}
