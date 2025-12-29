package com.rt.importer.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.rt.importer.entity.MessageSubscriptionEntity;

public interface MessageSubscriptionRepository extends CrudRepository<MessageSubscriptionEntity, Long> {

    Optional<MessageSubscriptionEntity> findByElementInstanceKeyAndMessageName(long elementInstanceKey,
        String messageName);

    Optional<MessageSubscriptionEntity> findByProcessDefinitionKeyAndMessageName(long processDefinitionKey,
        String messageName);

}
