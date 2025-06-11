package b1nd.dodam.domain.rds.notice.repository;

import b1nd.dodam.domain.rds.notice.entity.Notice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Query("""
            select n from notice n
            join notice_division nd on nd.notice.id = n.id
            join nd.division d
            join division_member dm on dm.division = d
            where dm.member.id = :memberId
            and (:lastId is null or n.id < :lastId)
            and (:divisionId is null or d.id = :divisionId)
            order by n.createdAt desc
        """)
    List<Notice> findNoticesByMemberAndDivision(@Param("memberId") String memberId,
                                                @Param("divisionId") Long divisionId,
                                                @Param("lastId") Long lastId,
                                                Pageable pageable);

    @Query("""
            select n from notice n
            join notice_division nd on nd.notice.id = n.id
            where nd.division.id in :ids
            and (:lastId is null or n.id < :lastId)
            and n.title like %:keyword%
            order by n.createdAt desc
        """)
    List<Notice> findAllByNoticeStatus(@Param("keyword") String keyword,
                                       @Param("ids") List<Long> ids,
                                       @Param("lastId") Long lastId,
                                       Pageable pageable);

}
