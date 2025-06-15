package com.endpoint.analyticsservice.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "patient", groupId = "analytics-service")
    public void consumeEvent(byte[] event) {
        try {
            PatientEvent patientEvent = PatientEvent.parseFrom(event);
//            Perform analytics business logic

            log.info("Received patient event: {}", patientEvent.toString());
        } catch (InvalidProtocolBufferException e) {
            log.error("Error deserializing event: {}", e.getMessage());
        }
    }
}
