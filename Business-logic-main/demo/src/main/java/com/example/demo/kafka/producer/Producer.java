package com.example.demo.kafka.producer;

import com.example.demo.Dto.Requests.PerformPaymentRequest;
import com.example.demo.kafka.serializable.PerformPaymentRequestSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.Future;

public class Producer {
    private static final String BOOTSTRAP_SERVER = "127.0.0.1:9092";
    private static final String TOPIC_NAME = "perform-topic";

    private static final Logger logger = LoggerFactory.getLogger(Producer.class);
    private static KafkaProducer<String, PerformPaymentRequest> producer;

    public static void configureProducer() {
        // Create Producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, PerformPaymentRequestSerializer.class.getName());

        // Create Producer
        producer = new KafkaProducer<>(properties);
    }

    public static PerformPaymentRequest createPaymentRequest(int userId, String cardNum, String cardDate, String cardCvv, double cost, String address) {
        PerformPaymentRequest paymentRequest = new PerformPaymentRequest();
        paymentRequest.setUserId(userId);
        paymentRequest.setCardNum(cardNum);
        paymentRequest.setCardDate(cardDate);
        paymentRequest.setCardCvv(cardCvv);
        paymentRequest.setCost(cost);
        paymentRequest.setAddress(address);
        return paymentRequest;
    }


    public static void sendMessage(PerformPaymentRequest paymentRequest) {
        String key = String.valueOf(paymentRequest.getUserId());

        ProducerRecord<String, PerformPaymentRequest> record = new ProducerRecord<>(
                TOPIC_NAME,
                key,
                paymentRequest
        );

        Future<RecordMetadata> future = producer.send(record, (metadata, exception) -> {
            if (exception == null) {
                logger.info("Received new metadata, topic: " + metadata.topic()
                        + " partition: " + metadata.partition()
                        + " offset: " + metadata.offset()
                        + " timestamp: " + metadata.timestamp());
            } else {
                logger.error("Error producing: ", exception);
            }
        });

        // Wait for the message to be sent and acknowledgment to be received
        try {
            future.get();
        } catch (Exception e) {
            logger.error("Error while waiting for acknowledgment: ", e);
        }

        // Observing Kafka round-robin feature
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void closeProducer() {
        // Flush data and close producer
        producer.flush();
        producer.close();
    }
}

