package b1nd.dodam.domain.rds.club.repository;

import b1nd.dodam.domain.rds.club.entity.ClubStudent;
import b1nd.dodam.domain.rds.club.enumeration.ClubType;
import b1nd.dodam.domain.rds.member.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubMemberRepository extends JpaRepository<ClubStudent, Long> {
    List<ClubStudent> findAllByStudentAndClub_Type(Student student, ClubType clubType);
}
