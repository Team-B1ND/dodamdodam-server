package b1nd.dodam.domain.rds.group.repository;

import b1nd.dodam.domain.rds.group.entity.Group;
import b1nd.dodam.domain.rds.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {

}
