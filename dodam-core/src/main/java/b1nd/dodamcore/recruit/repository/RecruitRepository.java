package b1nd.dodamcore.recruit.repository;

import b1nd.dodamcore.recruit.domain.entity.Recruit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecruitRepository extends JpaRepository<Recruit, Integer> {

    @EntityGraph(attributePaths = {"writer"})
    Page<Recruit> findAllByOrderByIdDesc(Pageable pageRequest);

    @Query("select r from recruit r LEFT JOIN FETCH r.recruitFiles JOIN FETCH r.writer where r.id = :id")
    Optional<Recruit> findByIdWithJoin(int id);
}