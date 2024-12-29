package b1nd.dodam.restapi.notice.application.data.req;

import b1nd.dodam.domain.rds.division.entity.Division;
import b1nd.dodam.domain.rds.notice.entity.Notice;
import b1nd.dodam.domain.rds.notice.entity.NoticeDivision;

public record AddNoticeDivisionReq(
        Long DivisionId
) {
    public NoticeDivision toEntity(Notice notice, Division division){
        return NoticeDivision.builder()
                .notice(notice)
                .division(division)
                .build();
    }
}
