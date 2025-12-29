package com.rt.importer.repository;

import org.springframework.data.repository.CrudRepository;

import com.rt.importer.entity.ProcessEntity;

public interface ProcessRepository extends CrudRepository<ProcessEntity, Long> {

}
