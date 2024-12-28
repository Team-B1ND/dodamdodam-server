package b1nd.dodam.restapi.notice.application.data.req;

import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.notice.entity.Notice;
import b1nd.dodam.domain.rds.support.enumeration.FileType;

import java.util.List;

import static b1nd.dodam.domain.rds.notice.enumration.NoticeStatus.DRAFT;

public record GenerateNoticeReq (
  String title,
  String content,
  String fileUrl,
  FileType fileType
){
    public Notice toEntity(Member member) {
        return Notice.builder()
                .title(title)
                .content(content)
                .fileUrl(fileUrl)
                .fileType(fileType)
                .noticeStatus(DRAFT)
                .member(member)
                .build();
    }
}
