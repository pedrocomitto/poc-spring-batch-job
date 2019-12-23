package com.pedrocomitto.poc.job.batch.processor;

import com.pedrocomitto.poc.job.AnyItemService;
import com.pedrocomitto.poc.job.domain.entity.AnyItemEntity;
import com.pedrocomitto.poc.job.repository.AnyItemRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
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
@EnableBatchProcessing
public class BatchConfig {

    private AnyItemRepository anyItemRepository;

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final AnyItemService anyItemService;

    public BatchConfig(AnyItemRepository anyItemRepository,
                       JobBuilderFactory jobBuilderFactory,
                       StepBuilderFactory stepBuilderFactory,
                       AnyItemService anyItemService) {
        this.anyItemRepository = anyItemRepository;
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.anyItemService = anyItemService;
    }


    @Bean
    public FlatFileItemReader<AnyItemEntity> reader() {
        FlatFileItemReader<AnyItemEntity> reader = new FlatFileItemReader<>();

        reader.setResource(new ClassPathResource("sample-data.csv"));

        reader.setLineMapper(new DefaultLineMapper<AnyItemEntity>() {
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    { setNames(new String[]{"info1", "info2"}); }
                });
                setFieldSetMapper(new BeanWrapperFieldSetMapper<AnyItemEntity>() {
                    { setTargetType(AnyItemEntity.class); }
                });
            }
        });

        return reader;
    }

    @Bean
    public AnyItemProcessor processor() {
        return new AnyItemProcessor(anyItemService);
    }

    @Bean
    public RepositoryItemWriter writer() {
        return new RepositoryItemWriterBuilder<AnyItemEntity>()
                .repository(anyItemRepository)
                .methodName("save").build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<AnyItemEntity, AnyItemEntity>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("sem nome")
                .incrementer(new RunIdIncrementer())
                .flow(step1())
                .end()
                .build();
    }
}
