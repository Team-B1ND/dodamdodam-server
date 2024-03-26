package b1nd.dodamcore.recruit.domain.entity;

import b1nd.dodamcore.common.entity.BaseEntity;
import b1nd.dodamcore.member.domain.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity(name = "recruit")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class Recruit extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_writer_id")
    private Member writer; // 작성자

    @NotNull
    private String name;

    @NotNull
    private String location;

    @NotNull
    private String duty; //직무

    @Column(columnDefinition = "LONGTEXT")
    private String etc; //추가 내용

    @NotNull
    private int personnel; //모집 인원

    @NotNull
    private String image; //채용 공고 이미지

    @OneToMany(mappedBy = "recruit", cascade = CascadeType.ALL)
    private List<RecruitFile> recruitFiles = new ArrayList<>();

    @Builder
    public Recruit(Member writer, String name, String location, String duty, String etc, int personnel, String image) {
        this.writer = writer;
        this.name = name;
        this.location = location;
        this.duty = duty;
        this.etc = etc;
        this.personnel = personnel;
        this.image = image;
    }

    public void addRecruitFile(RecruitFile recruitFile) {
        this.recruitFiles.add(recruitFile);
        recruitFile.setRecruit(this);
    }

    public void updateRecruit(String name, String location, String duty, String etc, int personnel, String image, List<RecruitFile> recruitFiles) {
        this.name = name;
        this.location = location;
        this.duty = duty;
        this.etc = etc;
        this.personnel = personnel;
        this.image = image;
        this.recruitFiles = recruitFiles;
    }
}
