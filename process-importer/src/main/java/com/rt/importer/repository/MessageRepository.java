package com.rt.importer.repository;

import org.springframework.data.repository.CrudRepository;

import com.rt.importer.entity.MessageEntity;

public interface MessageRepository extends CrudRepository<MessageEntity, Long> {}
