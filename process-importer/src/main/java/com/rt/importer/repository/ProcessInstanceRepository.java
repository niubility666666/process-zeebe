package com.rt.importer.repository;

import org.springframework.data.repository.CrudRepository;

import com.rt.importer.entity.ProcessInstanceEntity;

public interface ProcessInstanceRepository extends CrudRepository<ProcessInstanceEntity, Long> {

}
