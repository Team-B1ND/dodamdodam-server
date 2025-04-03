package b1nd.dodam.restapi.notice.infrastructure.batch;

import b1nd.dodam.domain.rds.notice.entity.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final NoticeItemWriter noticeItemWriter;
    private final NoticeItemProcessor noticeItemProcessor;
    private final NoticelerItemReader noticelerItemReader;

    @Bean
    public Job noticeCrawlingJob() {
        return new JobBuilder("noticeCrawlingJob", jobRepository)
                .start(noticeCrawlingStep())
                .build();
    }

    @Bean
    public Step noticeCrawlingStep() {
        return new StepBuilder("noticeCrawlingStep", jobRepository)
                .<Notice, Notice>chunk(10, platformTransactionManager)
                .reader(noticelerItemReader)
                .processor(noticeItemProcessor)
                .writer(noticeItemWriter)
                .build();
    }
}
