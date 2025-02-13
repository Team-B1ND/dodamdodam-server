package b1nd.dodam.domain.rds.notice.repository;

import b1nd.dodam.domain.rds.notice.entity.Notice;
import b1nd.dodam.domain.rds.notice.enumration.NoticeStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Query("""
        select n from notice n
        join n.noticeDivisions nd
        join nd.division d
        join division_member dm on dm.division = d
        where dm.member.id = :memberId
        and d.id = :divisionId
        and n.id > :lastId
        """)
    List<Notice> findNoticesByMemberAndDivision(@Param("memberId") String memberId,
                                                @Param("divisionId") Long divisionId,
                                                @Param("lastId") Long lastId,
                                                Pageable pageable);

    @Query("""
        select n from notice n
        join n.noticeDivisions nd
        where nd.division.id in :ids
        and n.noticeStatus = :noticeStatus
        and n.id > :lastId
        and (:keyword is null or n.title like %:keyword%)
        """)
    List<Notice> findAllByNoticeStatus(@Param("keyword") String keyword,
                                       @Param("ids") List<Long> ids,
                                       @Param("noticeStatus") NoticeStatus noticeStatus,
                                       @Param("lastId") Long lastId, Pageable pageable);
}
