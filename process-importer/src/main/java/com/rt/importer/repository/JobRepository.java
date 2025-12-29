package com.rt.importer.repository;

import org.springframework.data.repository.CrudRepository;

import com.rt.importer.entity.JobEntity;

public interface JobRepository extends CrudRepository<JobEntity, Long> {}
