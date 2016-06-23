/**
 * Copyright (c) 2016 Intel Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.trustedanalytics.mqttlistener.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedanalytics.mqttlistener.kafka.KafkaProperties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;


import javax.annotation.PreDestroy;
import java.util.Properties;

public class KafkaConsumer implements MessageConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);
    private KafkaProperties kafkaProperties;
    private KafkaProducer<String, String> kafkaProducer;

    public KafkaConsumer(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    public void init() {
        LOGGER.debug("---- opening connection to Kafka");
        Properties producerConfig = new Properties();
        producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBrokers());
        producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProperties.getKeySerializer());
        producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProperties.getValueSerializer());
        this.kafkaProducer = new KafkaProducer<>(producerConfig);
    }

    public void destroy() {
        kafkaProducer.close();
        LOGGER.debug("---- closing connection to Kafka");
    }

    @Override
    public void consume(String message) {
        LOGGER.debug("---- sending message to Kafka");
        kafkaProducer.send(new ProducerRecord<>(kafkaProperties.getTopic(), message));
    }
}
