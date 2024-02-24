package b1nd.dodamcore.banner.domain.entity;

import b1nd.dodamcore.banner.domain.enums.BannerStatus;
import b1nd.dodamcore.common.util.ZonedDateTimeUtil;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Getter
@Entity(name = "banner")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String imageUrl;

    @NotNull
    private String redirectUrl;

    @NotNull
    private String title;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BannerStatus status;

    @NotNull
    private LocalDateTime expireAt;

    @Builder
    public Banner(String imageUrl, String redirectUrl, String title, LocalDateTime expireAt) {
        this.imageUrl = imageUrl;
        this.redirectUrl = redirectUrl;
        this.title = title;
        this.expireAt = expireAt;
        this.status = (ZonedDateTimeUtil.nowToLocalDateTime().isBefore(expireAt)) ? BannerStatus.ACTIVE : BannerStatus.DEACTIVATED;
    }

    public void updateBanner(String title, String image, String redirectUrl, LocalDateTime expireAt) {
        this.title = title;
        this.imageUrl = image;
        this.redirectUrl = redirectUrl;
        this.expireAt = expireAt;
    }

    public void activateStatus() {
        this.status = BannerStatus.ACTIVE;
    }

    public void deactivateStatus() {
        this.status = BannerStatus.DEACTIVATED;
    }
}