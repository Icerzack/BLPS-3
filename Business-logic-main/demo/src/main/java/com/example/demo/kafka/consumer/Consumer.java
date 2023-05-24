package com.example.demo.kafka.consumer;

import com.example.demo.Dto.Requests.PerformPaymentRequest;
import com.example.demo.kafka.serializable.PerformPaymentRequestDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

public class Consumer {
    private static final String BOOTSTRAP_SERVER = "127.0.0.1:9092";
    private static final String TOPIC_NAME = "perform-topic";
    private static final String GROUP_ID = "consumer-group";

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);
    private static KafkaConsumer<String, PerformPaymentRequest> consumer;

    public static void configureConsumer() {
        // Create Consumer properties
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, PerformPaymentRequestDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        // Create Consumer
        consumer = new KafkaConsumer<>(properties);
    }

    public static void subscribeToTopic() {
        // Subscribing to topic
        consumer.subscribe(Arrays.asList(TOPIC_NAME));
    }

    public static void startConsuming() {
        try {
            while (true) {
                ConsumerRecords<String, PerformPaymentRequest> records = consumer.poll(Duration.ofMillis(100));

                for (ConsumerRecord<String, PerformPaymentRequest> record : records) {
                    processMessage(record);

                    commitOffset(record);
                }
            }
        } catch (Exception e) {
            logger.error("Unexpected exception", e);
        }
    }

    private static void processMessage(ConsumerRecord<String, PerformPaymentRequest> record) {
        PerformPaymentRequest paymentRequest = record.value();
        logger.info("Received PerformPaymentRequest: " + paymentRequest);

        // Process the message
    }

    private static void commitOffset(ConsumerRecord<String, PerformPaymentRequest> record) {
        TopicPartition topicPartition = new TopicPartition(record.topic(), record.partition());
        OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(record.offset() + 1);
        consumer.commitSync(Collections.singletonMap(topicPartition, offsetAndMetadata));
        logger.info("Committed offset for partition " + topicPartition);
    }

    public static void closeConsumer() {
        consumer.close();
        logger.info("The consumer was closed");
    }
}
