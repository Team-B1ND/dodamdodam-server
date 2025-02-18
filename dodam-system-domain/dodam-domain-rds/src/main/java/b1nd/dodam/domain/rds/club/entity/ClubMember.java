package b1nd.dodam.domain.rds.club.entity;

import b1nd.dodam.domain.rds.club.enumeration.ClubPermission;
import b1nd.dodam.domain.rds.club.enumeration.ClubMemberStatus;
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
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "unique_member_club_index",
                columnNames = {
                        "fk_member_id",
                        "fk_club_id"
                }
        )
})
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
    private ClubMemberStatus clubMemberStatus;

    private int choiceNumber;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_club_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Club club;

    @Builder
    public ClubMember(Member member, Club club, int choiceNumber, ClubMemberStatus clubMemberStatus, ClubPermission permission) {
        this.member = member;
        this.club = club;
        this.choiceNumber = choiceNumber;
        this.clubMemberStatus = clubMemberStatus;
        this.permission = permission;
    }

    public void modifyStatus(ClubMemberStatus status) {
        this.clubMemberStatus = status;
    }
}
