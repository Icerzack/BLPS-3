package com.example.demo.kafka.serializable;

import com.example.demo.Dto.Requests.PerformPaymentRequest;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class PerformPaymentRequestDeserializer implements Deserializer<PerformPaymentRequest> {

    private final JsonDeserializer<PerformPaymentRequest> jsonDeserializer;

    public PerformPaymentRequestDeserializer() {
        this.jsonDeserializer = new JsonDeserializer<>(PerformPaymentRequest.class);
    }

    @Override
    public PerformPaymentRequest deserialize(String topic, byte[] data) {
        try {
            return jsonDeserializer.deserialize(topic, data);
        } catch (SerializationException e) {
            throw new SerializationException("Error deserializing PerformPaymentRequest", e);
        }
    }
}
