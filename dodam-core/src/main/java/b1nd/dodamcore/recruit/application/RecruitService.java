package b1nd.dodamcore.recruit.application;

import b1nd.dodamcore.common.util.ModifyUtil;
import b1nd.dodamcore.member.application.MemberSessionHolder;
import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodamcore.recruit.application.dto.req.RecruitReq;
import b1nd.dodamcore.recruit.application.dto.res.RecruitPageRes;
import b1nd.dodamcore.recruit.application.dto.res.RecruitRes;
import b1nd.dodamcore.recruit.domain.entity.Recruit;
import b1nd.dodamcore.recruit.domain.exception.RecruitNotFoundException;
import b1nd.dodamcore.recruit.repository.RecruitFileRepository;
import b1nd.dodamcore.recruit.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitService {

    private final RecruitRepository recruitRepository;
    private final RecruitFileRepository recruitFileRepository;
    private final MemberSessionHolder memberSessionHolder;

    public RecruitPageRes getRecruitsByPaging(int page) {

        Pageable pageRequest = PageRequest.of(page - 1, 10);
        Page<Recruit> recruitList = recruitRepository.findAllByOrderByIdDesc(pageRequest);

        int pageNum = recruitList.getTotalPages() - recruitList.getNumber();
        Integer nextPage = pageNum <= 1 ? null : pageNum;

        return RecruitPageRes.of(recruitList, nextPage);
    }

    public RecruitRes getRecruitById(int id) {

        Recruit recruit = recruitRepository.findByIdWithJoin(id)
                .orElseThrow(RecruitNotFoundException::new);

        return RecruitRes.of(recruit);
    }

    @Transactional(rollbackFor = Exception.class)
    public void createRecruit(RecruitReq createRecruitReq) {

        Member member = memberSessionHolder.current();
        Recruit recruit = createRecruitReq.mapToRecruit(member);

        recruitRepository.save(recruit);
    }

    @Transactional(rollbackFor = Exception.class)
    public void modifyRecruit(int id, RecruitReq dto) {

        Recruit recruit = recruitRepository.findById(id)
                .orElseThrow(RecruitNotFoundException::new);

        recruitFileRepository.deleteByRecruit_Id(id);

        recruit.updateRecruit(
                ModifyUtil.modifyIfNotNull(dto.name(), recruit.getName()),
                ModifyUtil.modifyIfNotNull(dto.location(), recruit.getLocation()),
                ModifyUtil.modifyIfNotNull(dto.duty(), recruit.getDuty()),
                ModifyUtil.modifyIfNotNull(dto.etc(), recruit.getEtc()),
                ModifyUtil.modifyIfNotZero(dto.personnel(), recruit.getPersonnel()),
                ModifyUtil.modifyIfNotNull(dto.image(), recruit.getImage()),
                dto.updateRecruitFile(recruit)
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteRecruit(int id) {

        Recruit recruit = recruitRepository.findById(id)
                .orElseThrow(RecruitNotFoundException::new);

        recruitRepository.delete(recruit);
    }
}
