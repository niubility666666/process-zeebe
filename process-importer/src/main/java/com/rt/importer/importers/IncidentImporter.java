package com.rt.importer.importers;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.rt.importer.entity.IncidentEntity;
import com.rt.importer.repository.IncidentRepository;

import io.camunda.zeebe.protocol.record.intent.IncidentIntent;
import io.zeebe.exporter.proto.Schema;

@Component
public class IncidentImporter {

    @Resource
    private IncidentRepository incidentRepository;

    public void importIncident(final Schema.IncidentRecord record) {

        final IncidentIntent intent = IncidentIntent.valueOf(record.getMetadata().getIntent());
        final long key = record.getMetadata().getKey();
        final long timestamp = record.getMetadata().getTimestamp();

        final IncidentEntity entity = incidentRepository.findById(key).orElseGet(() -> {
            final IncidentEntity newEntity = new IncidentEntity();
            newEntity.setKey(key);
            newEntity.setBpmnProcessId(record.getBpmnProcessId());
            newEntity.setProcessDefinitionKey(record.getProcessDefinitionKey());
            newEntity.setProcessInstanceKey(record.getProcessInstanceKey());
            newEntity.setElementInstanceKey(record.getElementInstanceKey());
            newEntity.setJobKey(record.getJobKey());
            newEntity.setErrorType(record.getErrorType());
            newEntity.setErrorMessage(record.getErrorMessage());
            return newEntity;
        });

        if (intent == IncidentIntent.CREATED) {
            entity.setCreated(timestamp);
            incidentRepository.save(entity);

        } else if (intent == IncidentIntent.RESOLVED) {
            entity.setResolved(timestamp);
            incidentRepository.save(entity);
        }
    }

}
