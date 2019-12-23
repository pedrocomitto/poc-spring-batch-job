package com.pedrocomitto.poc.job.repository;

import com.pedrocomitto.poc.job.domain.entity.AnyItemEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnyItemRepository extends CrudRepository<AnyItemEntity, Long> {
}
