package b1nd.dodam.domain.rds.notice.repository;

import b1nd.dodam.domain.rds.division.entity.Division;
import b1nd.dodam.domain.rds.notice.entity.Notice;
import b1nd.dodam.domain.rds.notice.entity.NoticeDivision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoticeDivisionRepository extends JpaRepository<NoticeDivision, Long> {
    Optional<NoticeDivision> findByDivision(Division division);
    List<NoticeDivision> findAllByDivision(Division division);
    List<NoticeDivision> findAllByNotice(Notice notice);

}
