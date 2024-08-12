package b1nd.dodam.domain.rds.recruitment.entity;

import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.support.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Entity(name = "recruit")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class Recruitment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_writer_id", nullable = false)
    private Member writer; // 작성자

    @NotNull
    private String name;

    @NotNull
    private String location;

    @NotNull
    private String duty; //직무

    @Column(columnDefinition = "LONGTEXT")
    private String etc; //추가 내용

    private Integer personnel; //모집 인원

    @NotNull
    private String image; //채용 공고 이미지

    @OneToMany(mappedBy = "recruit", cascade = CascadeType.ALL)
    private List<RecruitmentFile> recruitFiles = new ArrayList<>();

    @Builder
    public Recruitment(Member writer, String name, String location, String duty, String etc, int personnel, String image) {
        this.writer = writer;
        this.name = name;
        this.location = location;
        this.duty = duty;
        this.etc = etc;
        this.personnel = personnel;
        this.image = image;
    }

    public void addRecruitFile(RecruitmentFile recruitFile) {
        this.recruitFiles.add(recruitFile);
        recruitFile.setRecruit(this);
    }

    public void updateRecruit(String name, String location, String duty, String etc, Integer personnel, String image, List<RecruitmentFile> recruitFiles) {
        if(Objects.nonNull(name)) {
            this.name = name;
        }
        if(Objects.nonNull(location)) {
            this.location = location;
        }
        if(Objects.nonNull(duty)) {
            this.duty = duty;
        }
        this.etc = etc;
        if(0 < personnel) {
            this.personnel = personnel;
        }
        if(Objects.nonNull(image)) {
            this.image = image;
        }
        this.recruitFiles = recruitFiles;
    }

}
