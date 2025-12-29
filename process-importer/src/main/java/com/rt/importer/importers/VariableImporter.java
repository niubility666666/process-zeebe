package com.rt.importer.importers;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.rt.importer.entity.VariableEntity;
import com.rt.importer.repository.VariableRepository;

import io.zeebe.exporter.proto.Schema;

@Component
public class VariableImporter {

    @Resource
    private VariableRepository variableRepository;

    public void importVariable(final Schema.VariableRecord record) {
        final VariableEntity newVariable = new VariableEntity();
        newVariable.setPosition(record.getMetadata().getPosition());
        newVariable.setPartitionId(record.getMetadata().getPartitionId());
        if (!variableRepository.existsById(newVariable.getGeneratedIdentifier())) {
            newVariable.setTimestamp(record.getMetadata().getTimestamp());
            newVariable.setProcessInstanceKey(record.getProcessInstanceKey());
            newVariable.setName(record.getName());
            newVariable.setValue(record.getValue());
            newVariable.setScopeKey(record.getScopeKey());
            newVariable.setState(record.getMetadata().getIntent().toLowerCase());
            variableRepository.save(newVariable);
        }
    }

}
