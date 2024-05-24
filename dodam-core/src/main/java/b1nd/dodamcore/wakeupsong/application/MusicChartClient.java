package b1nd.dodamcore.wakeupsong.application;

import b1nd.dodamcore.wakeupsong.application.dto.res.ChartRes;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface MusicChartClient {

    CompletableFuture<List<ChartRes>> getList();
}
