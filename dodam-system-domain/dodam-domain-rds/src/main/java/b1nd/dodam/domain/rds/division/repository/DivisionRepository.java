package b1nd.dodam.domain.rds.division.repository;

import b1nd.dodam.domain.rds.division.entity.Division;import b1nd.dodam.domain.rds.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DivisionRepository extends JpaRepository<Division, Long> {
    @Query("""
    SELECT d FROM division d 
    JOIN division_member dm ON dm.division = d 
    WHERE dm.member = :member 
    AND d.id > :lastId 
    AND (:keyword IS NULL OR d.name LIKE %:keyword% OR d.description LIKE %:keyword%) 
    ORDER BY d.id ASC
""")
    List<Division> findByMember(
            @Param("member") Member member,
            @Param("lastId") Long lastId,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("""
    SELECT d FROM division d 
    WHERE d.id > :lastId 
    AND (:keyword IS NULL OR d.name LIKE %:keyword%)
    ORDER BY d.id ASC
    """)
    List<Division> findNextPageWithCursor(
            @Param("lastId") Long lastId,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    Optional<Division> findByName(String name);
}
