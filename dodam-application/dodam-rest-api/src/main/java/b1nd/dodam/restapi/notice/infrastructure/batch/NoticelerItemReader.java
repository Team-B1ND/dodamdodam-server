package b1nd.dodam.restapi.notice.infrastructure.batch;

import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.repository.MemberRepository;
import b1nd.dodam.domain.rds.notice.entity.Notice;
import b1nd.dodam.domain.rds.notice.enumration.NoticeStatus;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
//@StepScope
@RequiredArgsConstructor
public class NoticelerItemReader implements ItemReader<Notice> {

    private final MemberRepository memberRepository;
    private List<Notice> notices;
    private int nextIndex = 0;
    private final String url = "https://dgsw.dge.hs.kr";
    private final String additionalUrl = "/dgswh/na/ntt/selectNttList.do?mi=10091723&bbsId=10091723";
    private final String detailAdditionalUrl = "/dgswh/na/ntt/selectNttInfo.do?mi=10091723&bbsId=10091723";
    private Member testeacher;

//    @PostConstruct
//    public void init() {
//        this.testeacher = memberRepository.getById(String.valueOf(testeacher));
//        this.notices = fetchNoticesArticles();
//    }

    @PostConstruct
    public void init() throws IOException {
        this.testeacher = memberRepository.getById("re");
        this.notices = fetchNoticesArticles();
    }

    @Override
    public Notice read() throws Exception {
        if (nextIndex < notices.size()) {
            return notices.get(nextIndex++);
        } else {
            return null;
        }
    }

    public List<Notice> fetchNoticesArticles() throws IOException {
        List<Notice> notices = new ArrayList<>();

        Connection.Response response = Jsoup
                .connect(url + additionalUrl)
                .method(Connection.Method.GET)
                .execute();

        Map<String, String> cookies = response.cookies();

        Document document = Jsoup.connect(url + additionalUrl)
                .cookies(cookies)
                .get();
        log.info("cookies : {}", cookies);

        Elements posts = document.select("tr");
        log.info("posts : {}", posts);

        for (Element post : posts) {
            log.info("---------시작------------");
            log.info("post:{}", post);
            String nttSn = post.select("tr").select("td").select("a").attr("data-id");
//            String nttSn = post.attr("data-id").trim();
            log.info("href: {}", nttSn);
            if (nttSn.isEmpty()) continue;

            String detailUrl = url + detailAdditionalUrl + "&nttSn=" + nttSn;
            log.info("detailUrl: {}", detailUrl);

            Document detailDocument = Jsoup.connect(detailUrl)
                    .cookies(cookies)
                    .get();

            Elements detail = detailDocument.select(".bbs_ViewA");
//            log.info("detail : {}", detail);

            Elements titleElement = detail.select("h3");
            String title = (titleElement != null) ? titleElement.text().trim() : "제목 없음";
            String content = detail.select(".bbsV_cont").text().trim();
            String file = detail.select(".fname").text().trim();

            notices.add(new Notice(title, content, NoticeStatus.CREATED, testeacher));

            log.info("Title: {}", title);
            log.info("Content: {}", content);
            log.info("file: {}", file);
        }

        return notices;
    }

}