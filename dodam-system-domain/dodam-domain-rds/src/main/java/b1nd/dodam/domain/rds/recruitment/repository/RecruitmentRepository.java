package b1nd.dodam.domain.rds.recruitment.repository;

import b1nd.dodam.domain.rds.recruitment.entity.Recruitment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Integer> {

    @EntityGraph(attributePaths = {"writer"})
    Page<Recruitment> findAllByOrderByIdDesc(Pageable pageRequest);

    @Query("select r from recruit r LEFT JOIN FETCH r.recruitFiles JOIN FETCH r.writer where r.id = :id")
    Optional<Recruitment> findByIdWithJoin(int id);

}
