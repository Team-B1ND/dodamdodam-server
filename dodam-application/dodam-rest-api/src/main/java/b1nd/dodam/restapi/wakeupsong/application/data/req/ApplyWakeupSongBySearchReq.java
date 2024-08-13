package b1nd.dodam.restapi.wakeupsong.application.data.req;

import jakarta.validation.constraints.NotEmpty;

public record ApplyWakeupSongBySearchReq(@NotEmpty String title, @NotEmpty String artist) {}
