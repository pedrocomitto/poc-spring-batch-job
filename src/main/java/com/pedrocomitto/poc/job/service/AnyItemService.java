package com.pedrocomitto.poc.job.service;

import com.pedrocomitto.poc.job.domain.entity.AnyItemEntity;
import com.pedrocomitto.poc.job.repository.AnyItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class AnyItemService {

    private final AnyItemRepository anyItemRepository;

    public AnyItemService(final AnyItemRepository anyItemRepository) {
        this.anyItemRepository = anyItemRepository;
    }

    public AnyItemEntity normalize(final AnyItemEntity anyItemEntity) {
        log.info("M=normalize");

        anyItemEntity.setInfo1(anyItemEntity.getInfo1().toUpperCase());
        anyItemEntity.setInfo2(anyItemEntity.getInfo2().toUpperCase());

        return anyItemEntity;
    }

    public void updateItemAsSync(final AnyItemEntity anyItemEntity) {
        log.info("M=updateItemAsSync, id={}", anyItemEntity.getId());

        anyItemEntity.setDateSync(LocalDateTime.now());
        anyItemRepository.save(anyItemEntity);
    }
}
