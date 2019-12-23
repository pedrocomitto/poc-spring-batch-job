package com.pedrocomitto.poc.job.batch.writer;

import com.pedrocomitto.poc.job.domain.entity.AnyItemEntity;
import com.pedrocomitto.poc.job.repository.AnyItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@StepScope
@Qualifier("customWriter")
public class AnyItemCustomWriter implements ItemWriter<AnyItemEntity> {

    private final AnyItemRepository anyItemRepository;

    public AnyItemCustomWriter(final AnyItemRepository anyItemRepository) {
        this.anyItemRepository = anyItemRepository;
    }

    @Override
    public void write(List<? extends AnyItemEntity> items) throws Exception {
        log.info("custom writer working");
        items.forEach(anyItemRepository::save);
    }
}
