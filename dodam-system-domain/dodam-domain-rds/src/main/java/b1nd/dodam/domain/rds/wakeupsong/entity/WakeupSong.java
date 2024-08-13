package b1nd.dodam.domain.rds.wakeupsong.entity;

import b1nd.dodam.core.util.ZonedDateTimeUtil;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.support.entity.BaseEntity;
import b1nd.dodam.domain.rds.wakeupsong.enumeration.WakeupSongStatus;
import b1nd.dodam.domain.rds.wakeupsong.exception.NotWakeupSongApplicantException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Entity(name = "wakeup_song")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WakeupSong extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String thumbnailUrl;

    @NotNull
    private String videoTitle;

    @NotNull
    private String videoId;

    @NotNull
    private String videoUrl;

    @NotNull
    private String channelTitle;

    @NotNull
    @Enumerated(EnumType.STRING)
    private WakeupSongStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_checking_member_id")
    private Member checkingMember;

    private LocalDate playAt;

    @Builder
    public WakeupSong(String videoId, String videoUrl, String videoTitle, String channelTitle, String thumbnailUrl, Member member) {
        this.videoId = videoId;
        this.videoUrl = videoUrl;
        this.channelTitle = channelTitle;
        this.videoTitle = videoTitle;
        this.thumbnailUrl = thumbnailUrl;
        this.member = member;
        this.status = WakeupSongStatus.PENDING;
    }

    public void isApplicant(Member member) {
        if(!Objects.equals(this.member.getId(), member.getId())) {
            throw new NotWakeupSongApplicantException();
        }
    }

    public void allow(Member checkingMember) {
        this.checkingMember = checkingMember;
        this.playAt = ZonedDateTimeUtil.nowToLocalDate().plusDays(1);
        this.status = WakeupSongStatus.ALLOWED;
    }

    public void deny() {
        this.status = WakeupSongStatus.DENIED;
    }

}
