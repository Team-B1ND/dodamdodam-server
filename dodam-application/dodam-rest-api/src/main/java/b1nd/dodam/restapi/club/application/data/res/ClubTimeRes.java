package b1nd.dodam.restapi.club.application.data.res;

import b1nd.dodam.domain.rds.club.entity.ClubTime;

public record ClubTimeRes(
        String createStart,
        String createEnd,
        String applicantStart,
        String applicantEnd
) {
    public static ClubTimeRes of(ClubTime createTime, ClubTime applicantTime) {
        return new ClubTimeRes(
            createTime.getStart().toString(),
            createTime.getEnd().toString(),
            applicantTime.getStart().toString(),
            applicantTime.getEnd().toString()
        );
    }
}
