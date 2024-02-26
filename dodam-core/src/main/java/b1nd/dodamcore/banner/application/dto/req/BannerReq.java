package b1nd.dodamcore.banner.application.dto.req;

import b1nd.dodamcore.banner.domain.entity.Banner;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record BannerReq(@NotEmpty String title, @NotEmpty String image,
                        @NotEmpty String url, @NotNull LocalDateTime expireAt) {

    public Banner mapToBanner() {
        return Banner.builder()
                .title(title)
                .imageUrl(image)
                .redirectUrl(url)
                .expireAt(expireAt)
                .build();
    }
}
