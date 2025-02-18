package b1nd.dodam.domain.rds.club.entity;

import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.club.enumeration.ClubType;
import b1nd.dodam.domain.rds.member.entity.Teacher;
import b1nd.dodam.domain.rds.support.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "club")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Club extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    @NotNull
    private String shortDescription;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull
    private String subject;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ClubType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_teacher_id")
    private Teacher teacher;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ClubStatus state;

    @Builder
    public Club(String name, String shortDescription, String description, String subject, ClubType type, ClubStatus state) {
        this.name = name;
        this.shortDescription = shortDescription;
        this.description = description;
        this.subject = subject;
        this.type = type;
        this.state = state;
    }

    public void join(Teacher teacher) {
        this.teacher = teacher;
    }
}