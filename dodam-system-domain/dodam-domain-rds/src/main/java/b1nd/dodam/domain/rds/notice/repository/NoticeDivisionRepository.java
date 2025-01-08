package b1nd.dodam.domain.rds.notice.repository;

import b1nd.dodam.domain.rds.division.entity.Division;
import b1nd.dodam.domain.rds.notice.entity.NoticeDivision;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoticeDivisionRepository extends JpaRepository<NoticeDivision, Long> {

    @Query("SELECT n FROM notice_division n WHERE n.division = :division AND n.id > :lastId")
    List<NoticeDivision> findAllByDivisionWithPagination(
            @Param("division") Division division,
            @Param("lastId") Long lastId,
            Pageable pageable
    );

}
