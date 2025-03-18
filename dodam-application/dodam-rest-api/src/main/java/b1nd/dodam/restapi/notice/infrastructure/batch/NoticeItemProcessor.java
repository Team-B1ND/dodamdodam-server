package b1nd.dodam.restapi.notice.infrastructure.batch;

import b1nd.dodam.domain.rds.notice.entity.Notice;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class NoticeItemProcessor implements ItemProcessor<Notice, Notice> {

    @Override
    public Notice process(Notice item) throws Exception {
        // 여기서 필요한 추가적인 처리 로직을 넣을 수 있습니다.
        return item;  // 예시로 그대로 반환
    }
}