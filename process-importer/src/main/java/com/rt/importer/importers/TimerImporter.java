package com.rt.importer.importers;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.rt.importer.entity.TimerEntity;
import com.rt.importer.repository.TimerRepository;

import io.camunda.zeebe.protocol.record.intent.TimerIntent;
import io.zeebe.exporter.proto.Schema;

@Component
public class TimerImporter {

    @Resource
    private TimerRepository timerRepository;

    public void importTimer(final Schema.TimerRecord record) {

        final TimerIntent intent = TimerIntent.valueOf(record.getMetadata().getIntent());
        final long key = record.getMetadata().getKey();
        final long timestamp = record.getMetadata().getTimestamp();

        final TimerEntity entity = timerRepository.findById(key).orElseGet(() -> {
            final TimerEntity newEntity = new TimerEntity();
            newEntity.setKey(key);
            newEntity.setProcessDefinitionKey(record.getProcessDefinitionKey());
            newEntity.setTargetElementId(record.getTargetElementId());
            newEntity.setDueDate(record.getDueDate());
            newEntity.setRepetitions(record.getRepetitions());

            if (record.getProcessInstanceKey() > 0) {
                newEntity.setProcessInstanceKey(record.getProcessInstanceKey());
                newEntity.setElementInstanceKey(record.getElementInstanceKey());
            }

            return newEntity;
        });

        entity.setState(intent.name().toLowerCase());
        entity.setTimestamp(timestamp);
        timerRepository.save(entity);
    }

}
