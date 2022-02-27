package com.pedrocomitto.poc.job.batch.reader;

import com.pedrocomitto.poc.job.domain.entity.AnyItemEntity;
import com.pedrocomitto.poc.job.repository.AnyItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Queue;

@Slf4j
@Component
public class DatabaseAnyItemReader implements ItemReader<AnyItemEntity> {

    private final AnyItemRepository anyItemRepository;

    private final Queue<AnyItemEntity> queue = new ArrayDeque<>();

    public DatabaseAnyItemReader(final AnyItemRepository anyItemRepository) {
        this.anyItemRepository = anyItemRepository;
    }

    @Override
    public AnyItemEntity read()  {
        log.info("M=read, reading items with null sync date, currentPage={}", currentPage);

       if (queue.isEmpty()) {
           final PageRequest pageRequest = PageRequest.of(0, 200);
           final Slice<AnyItemEntity> page = anyItemRepository.findAll(pageRequest);
           queue.addAll(page.getContent());
       }

       return queue.isEmpty() ? null : queue.poll();
    }

}
