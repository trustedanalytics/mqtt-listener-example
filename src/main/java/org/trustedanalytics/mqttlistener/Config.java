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
package org.trustedanalytics.mqttlistener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.trustedanalytics.mqttlistener.consumer.EchoConsumer;
import org.trustedanalytics.mqttlistener.consumer.KafkaConsumer;
import org.trustedanalytics.mqttlistener.consumer.MessageConsumer;
import org.trustedanalytics.mqttlistener.ingestion.Ingestion;
import org.trustedanalytics.mqttlistener.ingestion.MqttProperties;
import org.trustedanalytics.mqttlistener.ingestion.OnMessageArrived;
import org.trustedanalytics.mqttlistener.kafka.KafkaProperties;

@Configuration
public class Config {

    private static final Logger LOG = LoggerFactory.getLogger(Config.class);

    @Autowired
    private MqttProperties mqttProperties;
    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean(initMethod = "init", destroyMethod = "destroy")
    protected MessageConsumer consumer() {
        return new KafkaConsumer(kafkaProperties);
    }

    @Bean(initMethod = "init", destroyMethod = "destroy")
    protected Ingestion ingestion(MessageConsumer consumer) {
        return new Ingestion(mqttProperties, new OnMessageArrived(consumer::consume));
    }
}
