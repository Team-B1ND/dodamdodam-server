package b1nd.dodam.domain.rds.member.repository;

import b1nd.dodam.domain.rds.member.entity.DormitoryManageMember;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.exception.DormitoryManageMemberNotFoundExcpetion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DormitoryManageMemberRepository extends JpaRepository<DormitoryManageMember, Integer> {

    default DormitoryManageMember getByMember(Member member) {
        return findByMember(member)
                .orElseThrow(DormitoryManageMemberNotFoundExcpetion::new);
    }

    Optional<DormitoryManageMember> findByMember(Member member);

    boolean existsByMember(Member member);

    void deleteByMember(Member member);

}
