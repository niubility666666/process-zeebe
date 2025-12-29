package com.rt.importer.importers;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.rt.importer.entity.JobEntity;
import com.rt.importer.repository.JobRepository;

import io.camunda.zeebe.protocol.record.intent.JobIntent;
import io.zeebe.exporter.proto.Schema;

@Component
public class JobImporter {

    @Resource
    private JobRepository jobRepository;

    public void importJob(final Schema.JobRecord record) {
        final JobIntent intent = JobIntent.valueOf(record.getMetadata().getIntent());
        final long key = record.getMetadata().getKey();
        final long timestamp = record.getMetadata().getTimestamp();

        final JobEntity entity = jobRepository.findById(key).orElseGet(() -> {
            final JobEntity newEntity = new JobEntity();
            newEntity.setKey(key);
            newEntity.setProcessInstanceKey(record.getProcessInstanceKey());
            newEntity.setElementInstanceKey(record.getElementInstanceKey());
            newEntity.setJobType(record.getType());
            return newEntity;
        });

        entity.setState(intent.name().toLowerCase());
        entity.setTimestamp(timestamp);
        entity.setWorker(record.getWorker());
        entity.setRetries(record.getRetries());
        jobRepository.save(entity);
    }

}
