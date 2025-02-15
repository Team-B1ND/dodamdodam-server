package b1nd.dodam.restapi.notice.application.data.res;

import b1nd.dodam.domain.rds.notice.entity.NoticeFile;
import b1nd.dodam.domain.rds.support.enumeration.FileType;

import java.util.List;

public record NoticeFileRes(
        String fileUrl,
        String fileName,
        FileType fileType
) {

    public static List<NoticeFileRes> of(List<NoticeFile> files) {
        return files.stream()
                .map(NoticeFileRes::of)
                .toList();
    }

    public static NoticeFileRes of(NoticeFile file) {
        return new NoticeFileRes(
                file.getFileUrl(),
                file.getFileName(),
                file.getFileType()
        );
    }

}
