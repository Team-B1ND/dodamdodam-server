package b1nd.dodam.restapi.notice.infrastructure.batch;

import b1nd.dodam.domain.rds.notice.entity.Notice;
import b1nd.dodam.domain.rds.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NoticeItemWriter implements ItemWriter<Notice> {

    private final NoticeRepository noticeRepository;

    @Override
    public void write(Chunk<? extends Notice> chunk) throws Exception {
        noticeRepository.saveAll(chunk);
    }
}
