package b1nd.dodamcore.nightstudy.application.dto.res;

import b1nd.dodamcore.member.application.dto.res.StudentRes;
import b1nd.dodamcore.nightstudy.domain.entity.NightStudy;
import b1nd.dodamcore.nightstudy.domain.enums.NightStudyStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record NightStudyRes(Long id,
                            String content,
                            NightStudyStatus status,
                            Boolean doNeedPhone, String reasonForPhone,
                            StudentRes student,
                            String place,
                            LocalDate startAt, LocalDate endAt,
                            LocalDateTime createdAt, LocalDateTime modifiedAt) {
    public static NightStudyRes of(NightStudy nightStudy) {
        return new NightStudyRes(
                nightStudy.getId(),
                nightStudy.getContent(),
                nightStudy.getStatus(),
                nightStudy.getDoNeedPhone(), nightStudy.getReasonForPhone(),
                StudentRes.of(nightStudy.getStudent()),
                nightStudy.getPlace().getPlace(),
                nightStudy.getStartAt(), nightStudy.getEndAt(),
                nightStudy.getCreatedAt(), nightStudy.getModifiedAt()
        );
    }
}