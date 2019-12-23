package com.pedrocomitto.poc.job.batch.processor;

import com.pedrocomitto.poc.job.AnyItemService;
import com.pedrocomitto.poc.job.domain.entity.AnyItemEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@StepScope
public class AnyItemProcessor implements ItemProcessor<AnyItemEntity, AnyItemEntity> {

    private final AnyItemService anyItemService;

    public AnyItemProcessor(AnyItemService anyItemService) {
        this.anyItemService = anyItemService;
    }

    @Override
    public AnyItemEntity process(AnyItemEntity item) {
        log.info("processing item={}", item.toString());

        return anyItemService.normalize(item);
    }
}
