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
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
//@StepScope
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

    @PostConstruct
    public void init() throws IOException {
        this.teacher = memberRepository.getById("re");
        fetchNoticesArticlesAsync().thenAccept(notices -> this.notices = notices);
    }

    @Override
    public Notice read() {
        if (nextIndex < notices.size()) {
            return notices.get(nextIndex++);
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
                    String title = doc.select("h3").text().trim();
                    String content = doc.select(".bbsV_cont").text().trim();
                    String file = doc.select(".fname").text().trim();

                    log.info("title : {}", title);
                    log.info("content : {}", content);
                    log.info("file : {}", file);
                    return new Notice(title, content, NoticeStatus.CREATED, teacher);
                });
    }

    private Mono<Document> fetchDocument(String url, Map<String, String> cookies) {
        return webClient.batchGet(url, cookies)
                .map(Jsoup::parse)
                .onErrorReturn(new Document(""));
    }
}
