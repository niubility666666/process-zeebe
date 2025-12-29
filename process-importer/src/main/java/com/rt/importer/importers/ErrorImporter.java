package com.rt.importer.importers;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.rt.importer.entity.ErrorEntity;
import com.rt.importer.repository.ErrorRepository;

import io.zeebe.exporter.proto.Schema;

@Component
public class ErrorImporter {

    @Resource
    private ErrorRepository errorRepository;

    public void importError(final Schema.ErrorRecord record) {

        final Schema.RecordMetadata metadata = record.getMetadata();
        final long position = metadata.getPosition();
        ErrorEntity errorEntity = errorRepository.findById(position).orElseGet(() -> {
            final var newEntity = new ErrorEntity();
            newEntity.setPosition(position);
            newEntity.setErrorEventPosition(record.getErrorEventPosition());
            newEntity.setProcessInstanceKey(record.getProcessInstanceKey());
            newEntity.setExceptionMessage(record.getExceptionMessage());
            newEntity.setStacktrace(record.getStacktrace());
            newEntity.setTimestamp(metadata.getTimestamp());
            return newEntity;
        });
        errorRepository.save(errorEntity);
    }
}
