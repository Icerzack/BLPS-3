package com.example.demo.kafka.serializable;

import com.example.demo.Dto.Requests.PerformPaymentRequest;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class PerformPaymentRequestSerializer implements Serializer<PerformPaymentRequest> {

    private final JsonSerializer<PerformPaymentRequest> jsonSerializer = new JsonSerializer<>();

    @Override
    public byte[] serialize(String topic, PerformPaymentRequest data) {
        try {
            return jsonSerializer.serialize(topic, data);
        } catch (SerializationException e) {
            throw new SerializationException("Error serializing PerformPaymentRequest", e);
        }
    }
}

