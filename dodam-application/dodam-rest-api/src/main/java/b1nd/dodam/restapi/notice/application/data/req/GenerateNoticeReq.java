package b1nd.dodam.restapi.notice.application.data.req;

import b1nd.dodam.domain.rds.division.entity.Division;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.notice.entity.Notice;
import b1nd.dodam.domain.rds.notice.entity.NoticeDivision;
import b1nd.dodam.domain.rds.support.enumeration.FileType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static b1nd.dodam.domain.rds.notice.enumration.NoticeStatus.DRAFT;

public record GenerateNoticeReq (
  String title,
  String content,
  Map<String, FileType> file,
  List<Long> divisionId
){
    public Notice toEntity(Member member) {
        return Notice.builder()
                .title(title)
                .content(content)
                .noticeStatus(DRAFT)
                .member(member)
                .build();
    }

    public List<NoticeDivision> toEntity(Notice notice, List<Division> divisions){
        return divisions.parallelStream()
                .map(division -> GenerateNoticeReq.toEntity(notice, division))
                .collect(Collectors.toList());
    }

    public static NoticeDivision toEntity(Notice notice, Division division){
        return NoticeDivision.builder()
                .notice(notice)
                .division(division)
                .build();
    }

}
