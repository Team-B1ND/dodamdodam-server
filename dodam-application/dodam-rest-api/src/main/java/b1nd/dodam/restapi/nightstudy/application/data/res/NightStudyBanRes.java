package b1nd.dodam.restapi.nightstudy.application.data.res;

import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyBan;
import b1nd.dodam.restapi.member.application.data.res.StudentWithImageRes;

import java.time.LocalDate;
import java.util.List;

public record NightStudyBanRes(
        Long id,
        StudentWithImageRes student,
        String banReason,
        LocalDate started,
        LocalDate ended) {
    public static List<NightStudyBanRes> of(List<NightStudyBan> nightStudyBans) {
        return nightStudyBans
                .stream()
                .map(NightStudyBanRes::of)
                .toList();
    }

    public static NightStudyBanRes of(NightStudyBan nightStudyBan) {
        return new NightStudyBanRes(
                nightStudyBan.getId(),
                StudentWithImageRes.of(nightStudyBan.getStudent()),
                nightStudyBan.getReason(),
                nightStudyBan.getStarted(),
                nightStudyBan.getEnded()
        );
    }
}
