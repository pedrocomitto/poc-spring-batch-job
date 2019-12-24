package com.pedrocomitto.poc.job.batch.job;

import com.pedrocomitto.poc.job.batch.LoggerJobExecutionListener;
import com.pedrocomitto.poc.job.batch.reader.DatabaseAnyItemReader;
import com.pedrocomitto.poc.job.batch.writer.DatabaseAnyItemWriter;
import com.pedrocomitto.poc.job.domain.entity.AnyItemEntity;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final DatabaseAnyItemReader databaseAnyItemReader;

    private final DatabaseAnyItemWriter databaseAnyItemWriter;

    public DatabaseJobConfig(final JobBuilderFactory jobBuilderFactory,
                             final StepBuilderFactory stepBuilderFactory,
                             final DatabaseAnyItemReader databaseAnyItemReader,
                             final DatabaseAnyItemWriter databaseAnyItemWriter) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.databaseAnyItemReader = databaseAnyItemReader;
        this.databaseAnyItemWriter = databaseAnyItemWriter;
    }

    @Bean
    public Job databaseJob() {
        final Step step = stepBuilderFactory.get("database-step")
                .<AnyItemEntity, AnyItemEntity>chunk(200)
                .reader(databaseAnyItemReader)
                .writer(databaseAnyItemWriter)
                .build();

        return jobBuilderFactory.get("database-job")
                .incrementer(new RunIdIncrementer())
                .listener(new LoggerJobExecutionListener())
                .start(step)
                .build();
    }
}
