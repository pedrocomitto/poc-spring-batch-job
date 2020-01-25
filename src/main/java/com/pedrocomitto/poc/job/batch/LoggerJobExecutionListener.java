package com.pedrocomitto.poc.job.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public class LoggerJobExecutionListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("beforeJob");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        final long duration = jobExecution.getEndTime().getTime() - jobExecution.getStartTime().getTime();

        log.info("M=afterJob, duration={}", duration);
    }

}
