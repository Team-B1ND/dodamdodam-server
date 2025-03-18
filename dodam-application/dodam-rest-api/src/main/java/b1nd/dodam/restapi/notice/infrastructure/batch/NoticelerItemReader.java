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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
                .connect(url)
                .method(Connection.Method.GET)
                .execute();

        Map<String, String> cookies = response.cookies();
        Document document = Jsoup.connect(url + additionalUrl)
                .cookies(cookies)
                .get();

        log.info("cookies : {}", cookies);

        Elements posts = document.select("td.bbs_tit > a");

        log.info("posts : {}", posts);
        for (Element post : posts) {
            String nttSn = post.attr("data-id");
            String bbsId = "10091704";
            String mi = "10091704";

            // 공지사항의 상세 URL 생성
            String detailUrl = url + additionalUrl + "&nttSn=" + nttSn;
            log.info("detailUrl: {}", detailUrl);

            // 공지사항 상세 페이지 크롤링
            Document detailDocument = Jsoup.connect(detailUrl)
                    .cookies(cookies)
                    .get();
            log.info("detailDocument: {}", detailDocument);

            // 제목, 내용, 파일 등 필요한 데이터 추출
            String title = detailDocument.select(".bbs_ViewA").text(); // 제목
            String content = detailDocument.select(".bbsV_cont").text(); // 내용
            String file = detailDocument.select(".fname").text(); // 첨부파일 정보

            // Notice 객체 생성 후 리스트에 추가
            notices.add(new Notice(title, content, NoticeStatus.CREATED, testeacher));
            log.info("Title: " + title);
            log.info("Content: " + content);
        }
        return notices;
    }
}