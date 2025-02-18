package b1nd.dodam.restapi.notice.application.data.req;

import b1nd.dodam.domain.rds.division.entity.Division;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.notice.entity.Notice;
import b1nd.dodam.domain.rds.notice.entity.NoticeDivision;
import b1nd.dodam.domain.rds.notice.entity.NoticeFile;
import b1nd.dodam.domain.rds.notice.enumration.NoticeStatus;
import b1nd.dodam.restapi.notice.application.data.File;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record GenerateNoticeReq (@NotEmpty String title, @NotEmpty String content, List<File> files,
                                 List<Long> divisions){

    public Notice toEntity(Member member) {
        Notice notice = Notice.builder()
                .title(title)
                .content(content)
                .noticeStatus(NoticeStatus.CREATED)
                .member(member)
                .build();

        return notice;
    }

    public List<NoticeFile> toNoticeFiles(Notice notice) {
        if (files == null || files.isEmpty()) {
            return List.of();
        }
        return files.stream()
                .map(file -> NoticeFile.builder()
                        .fileUrl(file.url())
                        .fileName(file.name())
                        .fileType(file.fileType())
                        .notice(notice)
                        .build())
                .toList();
    }

    public List<NoticeDivision> toEntity(Notice notice, List<Division> divisions) {
        return divisions.stream()
                .map(division -> GenerateNoticeReq.toEntity(notice, division))
                .toList();
    }

    public static NoticeDivision toEntity(Notice notice, Division division){
        return NoticeDivision.builder()
                .notice(notice)
                .division(division)
                .build();
    }

}