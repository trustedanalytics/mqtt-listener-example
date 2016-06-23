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
package org.trustedanalytics.mqttlistener.ingestion;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class OnMessageArrived implements MqttCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(OnMessageArrived.class);

    private final Consumer<String> consumer;

    public OnMessageArrived(Consumer<String> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void connectionLost(Throwable throwable) {
        LOGGER.error("Connection lost. " + throwable);
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        LOGGER.debug("topic: {}, message: {}", topic, mqttMessage);
        String message = String.format("topic: %s, message: %s", topic, mqttMessage);
        consumer.accept(message);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        LOGGER.debug("deliveryComplete: {}",  iMqttDeliveryToken);
    }
}

