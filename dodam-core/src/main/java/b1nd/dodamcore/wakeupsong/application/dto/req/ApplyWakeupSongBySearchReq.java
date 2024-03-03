package b1nd.dodamcore.wakeupsong.application.dto.req;

import jakarta.validation.constraints.NotEmpty;

public record ApplyWakeupSongBySearchReq(@NotEmpty String title, @NotEmpty String artist) {
}