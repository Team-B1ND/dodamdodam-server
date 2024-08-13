package b1nd.dodam.domain.rds.recruitment.service;

import b1nd.dodam.domain.rds.recruitment.entity.Recruitment;
import b1nd.dodam.domain.rds.recruitment.exception.RecruitNotFoundException;
import b1nd.dodam.domain.rds.recruitment.repository.RecruitmentFileRepository;
import b1nd.dodam.domain.rds.recruitment.repository.RecruitmentRepository;
import b1nd.dodam.domain.rds.recruitment.service.data.RecruitPageRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecruitmentService {

    private final RecruitmentRepository recruitmentRepository;
    private final RecruitmentFileRepository recruitmentFileRepository;

    public void save(Recruitment recruitment) {
        recruitmentRepository.save(recruitment);
    }

    public void deleteById(int id) {
        recruitmentRepository.deleteById(id);
    }

    public void deleteFileByRecruitmentId(int id) {
        recruitmentFileRepository.deleteByRecruit_Id(id);
    }

    public Recruitment getById(int id) {
        return recruitmentRepository.findByIdWithJoin(id)
                .orElseThrow(RecruitNotFoundException::new);
    }

    public RecruitPageRes getAllOrderByIdDesc(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Recruitment> recruitmentPage = recruitmentRepository.findAllByOrderByIdDesc(pageable);
        int pageNum = recruitmentPage.getTotalPages() - recruitmentPage.getNumber();
        return RecruitPageRes.of(recruitmentPage, pageNum <= 1 ? null : pageNum);
    }

}
