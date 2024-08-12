package b1nd.dodam.domain.rds.recruitment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Entity(name = "recruit_file")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitmentFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_recruit_id", nullable = false)
    private Recruitment recruit;

    @NotNull
    @Column(columnDefinition = "LONGTEXT")
    private String pdfUrl;

    @NotNull
    private String pdfName;

    @Builder
    public RecruitmentFile(Recruitment recruit, String pdfUrl, String pdfName) {
        this.recruit = recruit;
        this.pdfUrl = pdfUrl;
        this.pdfName = pdfName;
    }

}
