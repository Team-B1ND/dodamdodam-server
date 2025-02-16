package b1nd.dodam.domain.rds.notice.entity;

import b1nd.dodam.domain.rds.support.enumeration.FileType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "notice_file")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(columnDefinition = "LONGTEXT")
    private String fileUrl;

    @NotNull
    private String fileName;

    @NotNull
    private FileType fileType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_notice_id")
    private Notice notice;

    @Builder
    public NoticeFile(String fileUrl, String fileName, FileType fileType, Notice notice) {
        this.fileUrl = fileUrl;
        this.fileName = fileName;
        this.fileType = fileType;
        this.notice = notice;
    }
}
