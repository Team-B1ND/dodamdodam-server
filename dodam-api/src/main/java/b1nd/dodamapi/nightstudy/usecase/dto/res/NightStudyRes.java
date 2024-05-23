package b1nd.dodamapi.nightstudy.usecase.dto.res;

import b1nd.dodamcore.member.domain.vo.StudentRes;
import b1nd.dodamcore.nightstudy.domain.entity.NightStudy;
import b1nd.dodamcore.nightstudy.domain.enums.NightStudyStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record NightStudyRes(Long id,
                            String content,
                            NightStudyStatus status,
                            Boolean doNeedPhone, String reasonForPhone,
                            StudentRes student,
                            String place,
                            String rejectReason,
                            LocalDate startAt, LocalDate endAt,
                            LocalDateTime createdAt, LocalDateTime modifiedAt) {
    public static List<NightStudyRes> of(List<NightStudy> nightStudies) {
        return nightStudies.parallelStream()
                .map(NightStudyRes::of)
                .toList();
    }

    public static NightStudyRes of(NightStudy nightStudy) {
        return new NightStudyRes(
                nightStudy.getId(),
                nightStudy.getContent(),
                nightStudy.getStatus(),
                nightStudy.getDoNeedPhone(), nightStudy.getReasonForPhone(),
                StudentRes.of(nightStudy.getStudent()),
                nightStudy.getPlace().getPlace(),
                nightStudy.getRejectReason(),
                nightStudy.getStartAt(), nightStudy.getEndAt(),
                nightStudy.getCreatedAt(), nightStudy.getModifiedAt()
        );
    }
}