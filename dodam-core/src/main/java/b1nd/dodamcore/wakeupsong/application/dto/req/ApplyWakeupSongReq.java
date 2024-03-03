package b1nd.dodamcore.wakeupsong.application.dto.req;

import jakarta.validation.constraints.NotEmpty;

public record ApplyWakeupSongReq(@NotEmpty String videoUrl) {
}