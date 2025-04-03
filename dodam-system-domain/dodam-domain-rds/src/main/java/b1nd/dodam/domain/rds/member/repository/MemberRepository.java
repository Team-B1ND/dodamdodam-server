package b1nd.dodam.domain.rds.member.repository;

import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.enumeration.ActiveStatus;
import b1nd.dodam.domain.rds.member.enumeration.MemberRole;
import b1nd.dodam.domain.rds.member.exception.MemberNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByIdAndPw(String id, String pw);

    List<Member> findByCreatedAtAfter(LocalDateTime createdAt);

    @Query("SELECT m FROM member m LEFT JOIN student s ON m.id = s.member.id " +
            "WHERE m.status = :status " +
            "ORDER BY CASE WHEN s.id IS NOT NULL THEN 0 ELSE 1 END ASC, " +
            "s.grade ASC, s.room ASC, s.number ASC")
    List<Member> findByStatusOrderByStudent(ActiveStatus status);

    @Query("""
        SELECT m FROM member m
        LEFT JOIN student s ON s.member = m
        WHERE (:keyword IS NULL OR m.name LIKE %:keyword%) 
        AND (:grade IS NULL OR s.grade = :grade)
        AND (:role IS NULL OR m.role = :role)
        AND (:status IS NULL OR m.status = :status)
    """)
    Page<Member> searchMembers(
            @Param("keyword") String keyword,
            @Param("grade") Integer grade,
            @Param("role") MemberRole role,
            @Param("status") ActiveStatus status,
            Pageable pageable
    );

    List<Member> findByNameContains(String name);

    default Member getById(String id) {
        return findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }


    List<Member> findByModifiedAtAfter(LocalDateTime modifiedAt);

}
