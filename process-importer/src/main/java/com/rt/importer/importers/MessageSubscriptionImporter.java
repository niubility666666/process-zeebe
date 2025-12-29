package com.rt.importer.importers;

import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.rt.importer.entity.MessageSubscriptionEntity;
import com.rt.importer.repository.MessageSubscriptionRepository;

import io.camunda.zeebe.protocol.record.intent.MessageStartEventSubscriptionIntent;
import io.camunda.zeebe.protocol.record.intent.MessageSubscriptionIntent;
import io.zeebe.exporter.proto.Schema;

@Component
public class MessageSubscriptionImporter {
    @Resource
    private MessageSubscriptionRepository messageSubscriptionRepository;

    public void importMessageSubscription(final Schema.MessageSubscriptionRecord record) {

        final MessageSubscriptionIntent intent = MessageSubscriptionIntent.valueOf(record.getMetadata().getIntent());
        final long timestamp = record.getMetadata().getTimestamp();

        final MessageSubscriptionEntity entity = messageSubscriptionRepository
            .findByElementInstanceKeyAndMessageName(record.getElementInstanceKey(), record.getMessageName())
            .orElseGet(() -> {
                final MessageSubscriptionEntity newEntity = new MessageSubscriptionEntity();
                newEntity.setId(UUID.randomUUID().toString()); // message subscription doesn't have a key - it is always
                                                               // '-1'
                newEntity.setElementInstanceKey(record.getElementInstanceKey());
                newEntity.setMessageName(record.getMessageName());
                newEntity.setCorrelationKey(record.getCorrelationKey());
                newEntity.setProcessInstanceKey(record.getProcessInstanceKey());
                return newEntity;
            });

        entity.setState(intent.name().toLowerCase());
        entity.setTimestamp(timestamp);
        messageSubscriptionRepository.save(entity);
    }

    public void importMessageStartEventSubscription(final Schema.MessageStartEventSubscriptionRecord record) {
        final MessageStartEventSubscriptionIntent intent =
            MessageStartEventSubscriptionIntent.valueOf(record.getMetadata().getIntent());
        final long timestamp = record.getMetadata().getTimestamp();

        final MessageSubscriptionEntity entity = messageSubscriptionRepository
            .findByProcessDefinitionKeyAndMessageName(record.getProcessDefinitionKey(), record.getMessageName())
            .orElseGet(() -> {
                final MessageSubscriptionEntity newEntity = new MessageSubscriptionEntity();
                newEntity.setId(UUID.randomUUID().toString());
                newEntity.setMessageName(record.getMessageName());
                newEntity.setProcessDefinitionKey(record.getProcessDefinitionKey());
                newEntity.setTargetFlowNodeId(record.getStartEventId());
                return newEntity;
            });

        entity.setState(intent.name().toLowerCase());
        entity.setTimestamp(timestamp);
        messageSubscriptionRepository.save(entity);
    }
}
