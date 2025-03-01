package b1nd.dodam.restapi.club.application;

import b1nd.dodam.domain.rds.club.service.ClubTimeService;
import b1nd.dodam.restapi.club.application.data.req.ClubTimeReq;
import b1nd.dodam.restapi.support.data.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClubTimeUseCase {
    private final ClubTimeService clubTimeService;

    public Response save(ClubTimeReq req) {
        clubTimeService.setClubTime(req.toEntity());
        return Response.created("시간 설정됨");
    }
}
