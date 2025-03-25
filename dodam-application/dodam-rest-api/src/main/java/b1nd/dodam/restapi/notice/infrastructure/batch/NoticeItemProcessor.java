package b1nd.dodam.restapi.notice.infrastructure.batch;

import b1nd.dodam.domain.rds.notice.entity.Notice;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class NoticeItemProcessor implements ItemProcessor<Notice, Notice> {

    @Override
    public Notice process(Notice item) throws Exception {
        return item;
    }
}