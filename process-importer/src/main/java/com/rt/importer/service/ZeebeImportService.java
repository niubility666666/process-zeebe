package com.rt.importer.service;

import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.rt.importer.entity.HazelcastConfig;
import com.rt.importer.importers.ErrorImporter;
import com.rt.importer.importers.IncidentImporter;
import com.rt.importer.importers.JobImporter;
import com.rt.importer.importers.MessageImporter;
import com.rt.importer.importers.MessageSubscriptionImporter;
import com.rt.importer.importers.ProcessAndElementImporter;
import com.rt.importer.importers.TimerImporter;
import com.rt.importer.importers.VariableImporter;
import com.rt.importer.repository.HazelcastConfigRepository;
import com.hazelcast.core.HazelcastInstance;

import io.zeebe.exporter.proto.Schema;
import io.zeebe.hazelcast.connect.java.ZeebeHazelcast;

@Component
public class ZeebeImportService {

    @Resource
    private ProcessAndElementImporter processAndElementImporter;
    @Resource
    private VariableImporter variableImporter;
    @Resource
    private JobImporter jobImporter;
    @Resource
    private IncidentImporter incidentImporter;
    @Resource
    private MessageImporter messageImporter;
    @Resource
    private MessageSubscriptionImporter messageSubscriptionImporter;
    @Resource
    private TimerImporter timerImporter;
    @Resource
    private ErrorImporter errorImporter;
    @Resource
    private HazelcastConfigRepository hazelcastConfigRepository;

    public ZeebeHazelcast importFrom(final HazelcastInstance hazelcast) {

        final var hazelcastConfig = hazelcastConfigRepository.findById("cfg").orElseGet(() -> {
            final var config = new HazelcastConfig();
            config.setId("cfg");
            config.setSequence(-1);
            return config;
        });

        final var builder = ZeebeHazelcast.newBuilder(hazelcast)
            .addProcessListener(
                record -> ifEvent(record, Schema.ProcessRecord::getMetadata, processAndElementImporter::importProcess))
            .addProcessInstanceListener(record -> ifEvent(record, Schema.ProcessInstanceRecord::getMetadata,
                processAndElementImporter::importProcessInstance))
            .addIncidentListener(
                record -> ifEvent(record, Schema.IncidentRecord::getMetadata, incidentImporter::importIncident))
            .addJobListener(record -> ifEvent(record, Schema.JobRecord::getMetadata, jobImporter::importJob))
            .addVariableListener(
                record -> ifEvent(record, Schema.VariableRecord::getMetadata, variableImporter::importVariable))
            .addTimerListener(record -> ifEvent(record, Schema.TimerRecord::getMetadata, timerImporter::importTimer))
            .addMessageListener(
                record -> ifEvent(record, Schema.MessageRecord::getMetadata, messageImporter::importMessage))
            .addMessageSubscriptionListener(record -> ifEvent(record, Schema.MessageSubscriptionRecord::getMetadata,
                messageSubscriptionImporter::importMessageSubscription))
            .addMessageStartEventSubscriptionListener(
                record -> ifEvent(record, Schema.MessageStartEventSubscriptionRecord::getMetadata,
                    messageSubscriptionImporter::importMessageStartEventSubscription))
            .addErrorListener(errorImporter::importError).postProcessListener(sequence -> {
                hazelcastConfig.setSequence(sequence);
                hazelcastConfigRepository.save(hazelcastConfig);
            });

        if (hazelcastConfig.getSequence() >= 0) {
            builder.readFrom(hazelcastConfig.getSequence());
        } else {
            builder.readFromHead();
        }

        return builder.build();
    }

    private <T> void ifEvent(final T record, final Function<T, Schema.RecordMetadata> extractor,
        final Consumer<T> consumer) {
        final var metadata = extractor.apply(record);
        if (isEvent(metadata)) {
            consumer.accept(record);
        }
    }

    private boolean isEvent(final Schema.RecordMetadata metadata) {
        return metadata.getRecordType() == Schema.RecordMetadata.RecordType.EVENT;
    }

}
