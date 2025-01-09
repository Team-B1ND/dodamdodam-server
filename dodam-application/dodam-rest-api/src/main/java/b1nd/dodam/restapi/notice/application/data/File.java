package b1nd.dodam.restapi.notice.application.data;

import b1nd.dodam.domain.rds.notice.entity.NoticeFile;
import b1nd.dodam.domain.rds.support.enumeration.FileType;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.stream.Collectors;

public record File(@NotEmpty String url, @NotEmpty String name, @NotEmpty FileType fileType) {
    public static List<File> of(List<NoticeFile> noticeFiles){
        return noticeFiles.stream().map(
                noticeFile ->
                        new File(noticeFile.getFileUrl(),
                                noticeFile.getFileName(),
                                noticeFile.getFileType())
        ).collect(Collectors.toList());
    }
}
