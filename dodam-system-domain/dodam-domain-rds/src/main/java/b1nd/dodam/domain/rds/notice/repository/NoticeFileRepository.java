package b1nd.dodam.domain.rds.notice.repository;

import b1nd.dodam.domain.rds.notice.entity.Notice;
import b1nd.dodam.domain.rds.notice.entity.NoticeFile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeFileRepository extends JpaRepository<NoticeFile, Long> {

    @EntityGraph(attributePaths = {"notice"})
    List<NoticeFile> findAllByNoticeIn(List<Notice> notices);

}
