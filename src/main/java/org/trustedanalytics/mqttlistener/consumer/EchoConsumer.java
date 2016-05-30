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

public class EchoConsumer implements MessageConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(EchoConsumer.class);

    public EchoConsumer() {
    }

    @Override
    public void consume(String message) {
        LOG.info("---- received a value: {}", message);
    }

}
