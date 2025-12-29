package com.rt.importer.repository;

import org.springframework.data.repository.CrudRepository;

import com.rt.importer.entity.TimerEntity;

public interface TimerRepository extends CrudRepository<TimerEntity, Long> {}
