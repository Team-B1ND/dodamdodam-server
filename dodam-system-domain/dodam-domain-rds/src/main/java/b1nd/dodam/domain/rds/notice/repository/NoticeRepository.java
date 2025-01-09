package b1nd.dodam.domain.rds.notice.repository;

import b1nd.dodam.domain.rds.notice.entity.Notice;
import b1nd.dodam.domain.rds.notice.enumration.NoticeStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Query("SELECT n FROM notice n " +
            "JOIN n.noticeDivisions nd " +
            "JOIN nd.division d " +
            "JOIN division_member dm ON dm.division = d " +
            "WHERE dm.member.id = :memberId " +
            "AND d.id = :divisionId " +
            "AND n.id > :lastId")
    List<Notice> findNoticesByMemberAndDivision(@Param("memberId") String memberId,
                                                @Param("divisionId") Long divisionId,
                                                @Param("lastId") Long lastId,
                                                Pageable pageable);

    @Query("SELECT n FROM notice n " +
            "JOIN n.noticeDivisions nd " +
            "WHERE nd.division.id IN :ids " +
            "AND n.noticeStatus = :noticeStatus " +
            "AND n.id > :lastId")
    List<Notice> findAllByNoticeStatus(@Param("ids") List<Long> ids,
                                         @Param("noticeStatus") NoticeStatus noticeStatus,
                                         @Param("lastId") Long lastId, Pageable pageable);

}
