package com.rt.importer.repository;

import org.springframework.data.repository.CrudRepository;

import com.rt.importer.entity.ErrorEntity;

public interface ErrorRepository extends CrudRepository<ErrorEntity, Long> {

}
