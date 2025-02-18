package b1nd.dodam.domain.rds.club.entity;

import b1nd.dodam.domain.rds.club.enumeration.ClubPermission;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity(name = "club_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ClubPermission permission;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ClubStatus clubStatus;

    private int choiceNumber;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_division_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Club club;

    @Builder
    public ClubMember(Member member, Club club, int choiceNumber, ClubStatus clubStatus, ClubPermission permission) {
        this.member = member;
        this.club = club;
        this.choiceNumber = choiceNumber;
        this.clubStatus = clubStatus;
        this.permission = permission;
    }
}
