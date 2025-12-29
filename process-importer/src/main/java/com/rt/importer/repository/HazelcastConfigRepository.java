package com.rt.importer.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rt.importer.entity.HazelcastConfig;

@Repository
public interface HazelcastConfigRepository extends CrudRepository<HazelcastConfig, String> {}
