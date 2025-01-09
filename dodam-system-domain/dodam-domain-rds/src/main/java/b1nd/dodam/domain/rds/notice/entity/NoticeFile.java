package b1nd.dodam.domain.rds.notice.entity;

import b1nd.dodam.domain.rds.support.enumeration.FileType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Entity(name = "notice_file")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_notice_id")
    private Notice notice;

    @NotNull
    @Column(columnDefinition = "LONGTEXT")
    private String fileUrl;

    @NotNull
    private String fileName;

    @NotNull
    private FileType fileType;

    @Builder
    public NoticeFile(Long id, Notice notice, String fileUrl, String fileName, FileType fileType) {
        this.id = id;
        this.notice = notice;
        this.fileUrl = fileUrl;
        this.fileName = fileName;
        this.fileType = fileType;
    }

}
