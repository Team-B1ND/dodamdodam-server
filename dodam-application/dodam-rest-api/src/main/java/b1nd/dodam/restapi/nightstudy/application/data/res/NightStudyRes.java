package b1nd.dodam.restapi.nightstudy.application.data.res;

import b1nd.dodam.domain.rds.nightstudy.entity.NightStudy;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyType;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import b1nd.dodam.restapi.member.application.data.res.StudentRes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record NightStudyRes(
        Long id,
        String content,
        NightStudyType type,
        ApprovalStatus status,
        Boolean doNeedPhone,
        String reasonForPhone,
        StudentRes student,
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
                nightStudy.getType(),
                nightStudy.getStatus(),
                nightStudy.getDoNeedPhone(), nightStudy.getReasonForPhone(),
                StudentRes.of(nightStudy.getStudent()),
                nightStudy.getRejectReason(),
                nightStudy.getStartAt(), nightStudy.getEndAt(),
                nightStudy.getCreatedAt(), nightStudy.getModifiedAt()
        );
    }
}
