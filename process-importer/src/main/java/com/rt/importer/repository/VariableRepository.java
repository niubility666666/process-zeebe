package com.rt.importer.repository;

import org.springframework.data.repository.CrudRepository;

import com.rt.importer.entity.VariableEntity;

public interface VariableRepository extends CrudRepository<VariableEntity, String> {}
