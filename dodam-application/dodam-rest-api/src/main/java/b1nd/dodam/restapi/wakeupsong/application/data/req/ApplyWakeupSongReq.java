package b1nd.dodam.restapi.wakeupsong.application.data.req;

import jakarta.validation.constraints.NotEmpty;

public record ApplyWakeupSongReq(@NotEmpty String videoUrl) {}
