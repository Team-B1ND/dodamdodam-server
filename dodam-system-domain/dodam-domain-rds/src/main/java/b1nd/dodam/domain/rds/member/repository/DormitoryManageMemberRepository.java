package b1nd.dodam.domain.rds.member.repository;

import b1nd.dodam.domain.rds.member.entity.DormitoryManageMember;
import b1nd.dodam.domain.rds.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DormitoryManageMemberRepository extends JpaRepository<DormitoryManageMember, Integer> {

    boolean existsByMember(Member member);

    void deleteByMember(Member member);

}
