package b1nd.dodamcore.recruit.repository;

import b1nd.dodamcore.recruit.domain.entity.RecruitFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitFileRepository extends JpaRepository<RecruitFile, Integer> {

    void deleteByRecruit_Id(int Id);
}