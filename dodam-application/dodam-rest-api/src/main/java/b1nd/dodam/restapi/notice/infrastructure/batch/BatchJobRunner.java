package b1nd.dodam.restapi.notice.infrastructure.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BatchJobRunner {

    private final JobLauncher jobLauncher;
    private final Job job;

    @Scheduled(cron = "0 0 12 * * ?")
    private void executeBatchJob() {
        try {
            JobParameters jobParameter = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(job, jobParameter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
