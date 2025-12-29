package com.rt.importer.importers;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.rt.importer.entity.ElementInstanceEntity;
import com.rt.importer.entity.ProcessEntity;
import com.rt.importer.entity.ProcessInstanceEntity;
import com.rt.importer.repository.ElementInstanceRepository;
import com.rt.importer.repository.ProcessInstanceRepository;
import com.rt.importer.repository.ProcessRepository;

import io.camunda.zeebe.protocol.Protocol;
import io.camunda.zeebe.protocol.record.intent.Intent;
import io.camunda.zeebe.protocol.record.intent.ProcessInstanceIntent;
import io.zeebe.exporter.proto.Schema;

@Component
public class ProcessAndElementImporter {

    @Resource
    private ProcessRepository processRepository;
    @Resource
    private ProcessInstanceRepository processInstanceRepository;
    @Resource
    private ElementInstanceRepository elementInstanceRepository;

    public void importProcess(final Schema.ProcessRecord record) {
        final int partitionId = record.getMetadata().getPartitionId();
        if (partitionId == Protocol.DEPLOYMENT_PARTITION) {
            final ProcessEntity entity = new ProcessEntity();
            entity.setKey(record.getProcessDefinitionKey());
            entity.setBpmnProcessId(record.getBpmnProcessId());
            entity.setVersion(record.getVersion());
            entity.setResource(record.getResource().toStringUtf8());
            entity.setTimestamp(record.getMetadata().getTimestamp());
            processRepository.save(entity);
        }
    }

    public void importProcessInstance(final Schema.ProcessInstanceRecord record) {
        if (record.getProcessInstanceKey() == record.getMetadata().getKey()) {
            addOrUpdateProcessInstance(record);
        }
        addElementInstance(record);
    }

    private void addOrUpdateProcessInstance(final Schema.ProcessInstanceRecord record) {
        final Intent intent = ProcessInstanceIntent.valueOf(record.getMetadata().getIntent());
        final long timestamp = record.getMetadata().getTimestamp();
        final long processInstanceKey = record.getProcessInstanceKey();
        final ProcessInstanceEntity entity = processInstanceRepository.findById(processInstanceKey).orElseGet(() -> {
            final ProcessInstanceEntity newEntity = new ProcessInstanceEntity();
            newEntity.setPartitionId(record.getMetadata().getPartitionId());
            newEntity.setKey(processInstanceKey);
            newEntity.setBpmnProcessId(record.getBpmnProcessId());
            newEntity.setVersion(record.getVersion());
            newEntity.setProcessDefinitionKey(record.getProcessDefinitionKey());
            newEntity.setParentProcessInstanceKey(record.getParentProcessInstanceKey());
            newEntity.setParentElementInstanceKey(record.getParentElementInstanceKey());
            return newEntity;
        });

        if (intent == ProcessInstanceIntent.ELEMENT_ACTIVATED) {
            entity.setState("Active");
            entity.setStart(timestamp);
            processInstanceRepository.save(entity);
        } else if (intent == ProcessInstanceIntent.ELEMENT_COMPLETED) {
            entity.setState("Completed");
            entity.setEnd(timestamp);
            processInstanceRepository.save(entity);
        } else if (intent == ProcessInstanceIntent.ELEMENT_TERMINATED) {
            entity.setState("Terminated");
            entity.setEnd(timestamp);
            processInstanceRepository.save(entity);
        }
    }

    private void addElementInstance(final Schema.ProcessInstanceRecord record) {
        final ElementInstanceEntity entity = new ElementInstanceEntity();
        entity.setPartitionId(record.getMetadata().getPartitionId());
        entity.setPosition(record.getMetadata().getPosition());
        if (!elementInstanceRepository.existsById(entity.getGeneratedIdentifier())) {
            entity.setKey(record.getMetadata().getKey());
            entity.setIntent(record.getMetadata().getIntent());
            entity.setTimestamp(record.getMetadata().getTimestamp());
            entity.setProcessInstanceKey(record.getProcessInstanceKey());
            entity.setElementId(record.getElementId());
            entity.setFlowScopeKey(record.getFlowScopeKey());
            entity.setProcessDefinitionKey(record.getProcessDefinitionKey());
            entity.setBpmnElementType(record.getBpmnElementType());
            elementInstanceRepository.save(entity);
        }
    }

}
