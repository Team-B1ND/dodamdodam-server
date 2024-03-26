package b1nd.dodamcore.recruit.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Entity(name = "recruit_file")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_recruit_id")
    private Recruit recruit;

    @NotNull
    @Column(columnDefinition = "LONGTEXT")
    private String pdfUrl;

    @NotNull
    private String pdfName;

    @Builder
    public RecruitFile(Recruit recruit, String pdfUrl, String pdfName) {
        this.recruit = recruit;
        this.pdfUrl = pdfUrl;
        this.pdfName = pdfName;
    }
}
