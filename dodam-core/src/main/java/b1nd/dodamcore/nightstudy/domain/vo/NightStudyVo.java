package b1nd.dodamcore.nightstudy.domain.vo;

import b1nd.dodamcore.member.domain.vo.StudentVo;
import b1nd.dodamcore.nightstudy.domain.entity.NightStudy;
import b1nd.dodamcore.nightstudy.domain.enums.NightStudyStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record NightStudyVo(Long id,
                           String content,
                           NightStudyStatus status,
                           Boolean doNeedPhone, String reasonForPhone,
                           StudentVo student,
                           String place,
                           LocalDate startAt, LocalDate endAt,
                           LocalDateTime createdAt, LocalDateTime modifiedAt) {
    public static NightStudyVo of(NightStudy nightStudy) {
        return new NightStudyVo(
                nightStudy.getId(),
                nightStudy.getContent(),
                nightStudy.getStatus(),
                nightStudy.getDoNeedPhone(), nightStudy.getReasonForPhone(),
                StudentVo.of(nightStudy.getStudent()),
                nightStudy.getPlace().getPlace(),
                nightStudy.getStartAt(), nightStudy.getEndAt(),
                nightStudy.getCreatedAt(), nightStudy.getModifiedAt()
        );
    }
}