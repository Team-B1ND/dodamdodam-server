package b1nd.dodam.restapi.club.application;

import b1nd.dodam.domain.rds.club.entity.ClubTime;
import b1nd.dodam.domain.rds.club.enumeration.ClubTimeType;
import b1nd.dodam.domain.rds.club.service.ClubTimeService;
import b1nd.dodam.restapi.club.application.data.req.ClubTimeReq;
import b1nd.dodam.restapi.club.application.data.res.ClubTimeRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClubTimeUseCase {
    private final ClubTimeService clubTimeService;

    public Response save(ClubTimeReq req) {
        clubTimeService.setClubTime(req.toEntity());
        return Response.created("시간 설정 성공");
    }

    public ResponseData<ClubTimeRes> find() {
        ClubTime createTime = clubTimeService.getClubTime(ClubTimeType.CLUB_CREATED);
        ClubTime applicantTime = clubTimeService.getClubTime(ClubTimeType.CLUB_APPLICANT);
        return ResponseData.ok("시간 불러오기 성공", ClubTimeRes.of(createTime, applicantTime));
    }
}
