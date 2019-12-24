package com.pedrocomitto.poc.job.batch.writer;

import com.pedrocomitto.poc.job.domain.entity.AnyItemEntity;
import com.pedrocomitto.poc.job.service.AnyItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class DatabaseAnyItemWriter implements ItemWriter<AnyItemEntity> {

    private final AnyItemService anyItemService;

    public DatabaseAnyItemWriter(final AnyItemService anyItemService) {
        this.anyItemService = anyItemService;
    }

    @Override
    public void write(List<? extends AnyItemEntity> items) {
        log.info("M=write");
        items.forEach(anyItemService::generateInfo3);
    }
}
