package b1nd.dodamcore.member.repository;

import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodamcore.member.domain.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
}
