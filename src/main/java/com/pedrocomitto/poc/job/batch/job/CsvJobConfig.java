package com.pedrocomitto.poc.job.batch.job;

import com.pedrocomitto.poc.job.batch.LoggerJobExecutionListener;
import com.pedrocomitto.poc.job.batch.processor.CsvAnyItemProcessor;
import com.pedrocomitto.poc.job.domain.entity.AnyItemEntity;
import com.pedrocomitto.poc.job.repository.AnyItemRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class CsvJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final AnyItemRepository anyItemRepository;

    private final CsvAnyItemProcessor csvAnyItemProcessor;

    public CsvJobConfig(final JobBuilderFactory jobBuilderFactory,
                        final StepBuilderFactory stepBuilderFactory,
                        final AnyItemRepository anyItemRepository,
                        final CsvAnyItemProcessor csvAnyItemProcessor) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.anyItemRepository = anyItemRepository;
        this.csvAnyItemProcessor = csvAnyItemProcessor;
    }

    @Bean
    public Job csvJob() {
        final Step step = stepBuilderFactory.get("csv-step")
                .<AnyItemEntity, AnyItemEntity>chunk(500)
                .reader(reader())
                .processor(csvAnyItemProcessor)
                .writer(writer())
                .build();

        return jobBuilderFactory.get("csv-job")
                .incrementer(new RunIdIncrementer())
                .listener(new LoggerJobExecutionListener())
                .start(step)
                .build();
    }

    private FlatFileItemReader<AnyItemEntity> reader() {
        FlatFileItemReader<AnyItemEntity> reader = new FlatFileItemReader<>();

        reader.setResource(new ClassPathResource("sample-data.csv"));

        reader.setLineMapper(new DefaultLineMapper<AnyItemEntity>() {
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    { setNames("info1", "info2"); }
                });
                setFieldSetMapper(new BeanWrapperFieldSetMapper<AnyItemEntity>() {
                    { setTargetType(AnyItemEntity.class); }
                });
            }
        });

        return reader;
    }

    private RepositoryItemWriter<AnyItemEntity> writer() {
        return new RepositoryItemWriterBuilder<AnyItemEntity>()
                .repository(anyItemRepository)
                .methodName("save")
                .build();
    }

}
