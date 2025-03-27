package b1nd.dodam.restapi.notice.infrastructure.batch;

import b1nd.dodam.client.core.WebClientSupport;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.repository.MemberRepository;
import b1nd.dodam.domain.rds.notice.entity.Notice;
import b1nd.dodam.domain.rds.notice.enumration.NoticeStatus;
import b1nd.dodam.domain.redis.notice.service.NoticeRedisService;
import b1nd.dodam.restapi.support.async.AsyncConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class NoticelerItemReader implements ItemReader<Notice> {

    private final MemberRepository memberRepository;
    private final NoticeRedisService noticeRedisService;
    private final WebClientSupport webClient;
    private final AsyncConfig asyncConfig;
    private static final String URL = "https://dgsw.dge.hs.kr";
    private static final String ADDITIONAL_URL = "/dgswh/na/ntt/selectNttList.do?mi=10091723&bbsId=10091723";
    private static final String DETAIL_ADDITIONAL_URL = "/dgswh/na/ntt/selectNttInfo.do?mi=10091723&bbsId=10091723";
    private List<Notice> notices;
    private int nextIndex = 0;
    private Member teacher;
    private Map<String, String> cookies;
    private CompletableFuture<List<Notice>> fetchNoticesFuture;

    @PostConstruct
    public void init() {
        this.teacher = memberRepository.getById("re");
        fetchNoticesFuture = fetchNoticesArticlesAsync();
    }

    @Override
    public Notice read() {
        try {
            if (fetchNoticesFuture != null && fetchNoticesFuture.isDone()) {
                if (notices == null) notices = fetchNoticesFuture.get();
                if (nextIndex < notices.size()) return notices.get(nextIndex++);
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error while fetching notices", e);
        }
        return null;
    }

    public CompletableFuture<List<Notice>> fetchNoticesArticlesAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Connection.Response response = Jsoup
                        .connect(URL + ADDITIONAL_URL)
                        .method(Connection.Method.GET)
                        .execute();
                cookies = response.cookies();

                return fetchDocument(URL + ADDITIONAL_URL, cookies)
                        .flatMapMany(doc -> Flux.fromIterable(doc.select("tr")))
                        .flatMap(this::fetchNoticeDetailAsync)
                        .collectList()
                        .block();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, asyncConfig.batchTaskExecutor());
    }

    private Mono<Notice> fetchNoticeDetailAsync(Element post) {
        String nttSn = post.select("tr").select("td").select("a").attr("data-id");
        if (nttSn.isEmpty()) return Mono.empty();

        String cacheKey = "notice:" + nttSn;
        if (noticeRedisService.validateNotice(cacheKey)) {
            log.info("Skipping cached notice: {}", nttSn);
            return Mono.empty();
        }

        return fetchDocument(URL + DETAIL_ADDITIONAL_URL + "&nttSn=" + nttSn, cookies)
                .map(doc -> {
                    Element detailElement = doc.select(".bbs_ViewA").first();

                    if (detailElement == null) {
                        log.error("No detail found for notice: {}", nttSn);
                        return null;
                    }

                    String title = detailElement.select("h3").text().trim();
                    String content = detailElement.select(".bbsV_cont").text().trim();
                    String file = detailElement.select(".fname").text().trim();

                    log.info("Notice Title: {}", title);
                    log.info("Notice Content: {}", content);
                    log.info("Notice File: {}", file);

                    return new Notice(title, content, NoticeStatus.CREATED, teacher);
                });
    }

    private Mono<Document> fetchDocument(String url, Map<String, String> cookies) {
        return webClient.batchGet(url, cookies)
                .map(Jsoup::parse)
                .onErrorReturn(new Document(""));
    }

}
