package b1nd.dodam.domain.rds.notice.repository;

import b1nd.dodam.domain.rds.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

}
