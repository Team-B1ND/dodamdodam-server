package b1nd.dodam.restapi.notice.application;

import b1nd.dodam.domain.rds.division.entity.Division;
import b1nd.dodam.domain.rds.division.service.DivisionService;
import b1nd.dodam.domain.rds.notice.entity.Notice;
import b1nd.dodam.domain.rds.notice.entity.NoticeDivision;
import b1nd.dodam.domain.rds.notice.enumration.NoticeStatus;
import b1nd.dodam.domain.rds.notice.service.NoticeDivisionService;
import b1nd.dodam.domain.rds.notice.service.NoticeService;
import b1nd.dodam.restapi.notice.application.data.req.AddNoticeDivisionReq;
import b1nd.dodam.restapi.support.data.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class NoticeDivisionUseCase {

    private final NoticeService noticeService;
    private final DivisionService divisionService;
    private final NoticeDivisionService noticeDivisionService;

    public Response addDivision(Long noticeId, AddNoticeDivisionReq addNoticeDivisionReq){
        Notice notice = noticeService.getById(noticeId);
        Division division = divisionService.getById(addNoticeDivisionReq.DivisionId());
        NoticeDivision noticeDivision = addNoticeDivisionReq.toEntity(notice, division);
        noticeService.changeStatus(notice.getId(), NoticeStatus.CREATED);
        noticeDivisionService.save(noticeDivision);
        return Response.of(HttpStatus.OK, "성공");
    }

}
