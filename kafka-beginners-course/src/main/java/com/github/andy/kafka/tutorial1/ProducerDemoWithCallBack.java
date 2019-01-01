package com.github.andy.kafka.tutorial1;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.logging.Logger;

public class ProducerDemoWithCallBack {

    public static void main(String[] args) {

        final org.slf4j.Logger logger = LoggerFactory.getLogger(ProducerDemoWithCallBack.class);

        String bootstrapServers = "127.0.0.1:9092";

        // create producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //create the producer
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
        for (int i = 0; i < 10; i++) {
            //create producer record
            final ProducerRecord<String, String> record =
                    new ProducerRecord<String, String>("first_topic", "hello world" + Integer.toString(i));

            //send data
            producer.send(record, new Callback() {
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    //executes every time a record is sucessfully sent or an exception is thrown
                    if (e == null) {
                        //the record was sucessfully sent
                        logger.info("Received new Metadata. \n" +
                                "Topic: " + recordMetadata.topic() + "\n" +
                                "Partition: " + recordMetadata.partition() + "\n" +
                                "Offset: " + recordMetadata.offset() + "\n" +
                                "Timestamp: " + recordMetadata.timestamp());
                    } else {
                        logger.error("Error while producing", e);
                    }
                }
            });
        }
        producer.flush();
        producer.close();
    }
}
