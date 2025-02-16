package b1nd.dodam.domain.rds.notice.repository;

import b1nd.dodam.domain.rds.notice.entity.Notice;
import b1nd.dodam.domain.rds.notice.entity.NoticeDivision;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeDivisionRepository extends JpaRepository<NoticeDivision, Long> {

    void deleteByNotice(Notice notice);

}
