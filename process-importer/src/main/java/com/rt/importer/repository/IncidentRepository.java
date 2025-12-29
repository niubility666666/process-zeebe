package com.rt.importer.repository;

import org.springframework.data.repository.CrudRepository;

import com.rt.importer.entity.IncidentEntity;

public interface IncidentRepository extends CrudRepository<IncidentEntity, Long> {

}
