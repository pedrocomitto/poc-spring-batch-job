package com.pedrocomitto.poc.job.batch.processor;

import com.pedrocomitto.poc.job.domain.entity.AnyItemEntity;
import com.pedrocomitto.poc.job.repository.AnyItemRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private AnyItemRepository anyItemRepository;

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    public BatchConfig(AnyItemRepository anyItemRepository,
                       JobBuilderFactory jobBuilderFactory,
                       StepBuilderFactory stepBuilderFactory) {
        this.anyItemRepository = anyItemRepository;
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public FlatFileItemReader<AnyItemEntity> reader() {
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

    @Bean
    public RepositoryItemWriter<AnyItemEntity> writer() {
        return new RepositoryItemWriterBuilder<AnyItemEntity>()
                .repository(anyItemRepository)
                .methodName("save").build();
    }

    @Bean
    public Step step(AnyItemProcessor processor, ItemWriter<AnyItemEntity> writer) {
        return stepBuilderFactory.get("step")
                .<AnyItemEntity, AnyItemEntity>chunk(10)
                .reader(reader())
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    @Qualifier("stepWithCustomWriter")
    public Step stepWithCustomWriter(AnyItemProcessor processor, @Qualifier("customWriter") ItemWriter<AnyItemEntity> writer) {
        return stepBuilderFactory.get("step with custom writer")
                .<AnyItemEntity, AnyItemEntity>chunk(10)
                .reader(reader())
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job job(Step step) {
        return jobBuilderFactory.get("job")
                .incrementer(new RunIdIncrementer())
                .flow(step)
                .end()
                .build();
    }

    @Bean
    public Job jobWithAnotherStep(@Qualifier("stepWithCustomWriter") Step step) {
        return jobBuilderFactory.get("job with another step")
                .incrementer(new RunIdIncrementer())
                .flow(step)
                .end()
                .build();
    }
}
