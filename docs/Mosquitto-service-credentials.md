# How to access the credentials in the app
After binding Mosquitto service instance to the app, the following information is supplied by the platform:
```
{
  "mosquitto14": [
    {
      "label": "mosquitto14",
      "name": "mqtt-listener-messages",
      "plan": "free",
      "tags": [
        "mqtt",
        "messaging",
        "lightweight"
      ],
      "credentials": {
        "hostname": "10.0.4.4",
        "ports": {
          "1883/tcp": "33102"
        },
        "port": "33102",
        "username": "lh9lylotj4xoqefp",
        "password": "b0plvnjn6bfi5ouu"
      }
    }
  ]
}
```

If you take a look at `credentials` section, you find some key information here:

* hostname, port - the address of the broker
* username, password - credentials to be used when connecting


This information is stored in environment variables. There are many ways of obtaining the data in an app. 
In this example, we use a nice Spring Boot feature. 

We use property file that recognizes env variables (see [application-cloud.yml](../src/application-cloud.yml)): 

```
services:
  mqtt:
    hostname: tcp://${vcap.services.mqtt-listener-messages.credentials.hostname}
    port: ${vcap.services.mqtt-listener-messages.credentials.port}
    username: ${vcap.services.mqtt-listener-messages.credentials.username}
    password: ${vcap.services.mqtt-listener-messages.credentials.password}
    clientName: ${MQTT_CNAME}
    topic: ${MQTT_TOPIC}
  kafka:
    topic: ${KAFKA_TOPIC}
    brokers: ${vcap.services.kafka-mqtt-instance.credentials.uri}
```

Then we use a config object and automatically inject the properties:

```
@Component
@ConfigurationProperties(MqttProperties.PREFIX)
public class MqttProperties {

    protected static final String PREFIX = "services.mqtt";

    private String hostname;
    private String port;
    private String username;
    private String password;
    private String clientName;
    private String topic;
}
```

Spring Boot magic maps the properties from application.yml to this bean fields.

Then it gets autowired (in [Config.java](../src/main/java/org/trustedanalytics/mqttlistener/Config.java)) and used to configure MQTT connection ([Ingestion.java](../src/main/java/org/trustedanalytics/mqttlistener/ingestion/Ingestion.java)).  