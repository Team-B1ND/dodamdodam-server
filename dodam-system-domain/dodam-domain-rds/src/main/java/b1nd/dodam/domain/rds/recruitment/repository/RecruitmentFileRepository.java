package b1nd.dodam.domain.rds.recruitment.repository;

import b1nd.dodam.domain.rds.recruitment.entity.RecruitmentFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentFileRepository extends JpaRepository<RecruitmentFile, Integer> {

    void deleteByRecruit_Id(int Id);

}
