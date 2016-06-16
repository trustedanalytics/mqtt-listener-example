Developing MQTT application on Trusted Analytics Platform is no different from "normal" development of such an app.
 
The code is in general the same. TAP systematizes the way an application obtains credentials to services it needs to use.
The term for this is "service binging". This section explains how to create one in TAP and how the application can use it.

# Creating a service instance
Services available in TAP are presented in TAP's console Marketplace.
Go to the marketplace:
![The marketplace](images/tap-marketplace.png)
and select your organization and space.


Find Mosquitto and click on its icon:

![Mosquitto in marketplace](images/tap-mosquitto-marketplace.png)

Give the instance a name and click `Create new instance` button:
![Mosquitto in marketplace](images/tap-mosquitto-create.png)


> This example assumes the service is called `mqtt-listener-messages`. The name is used in `application.yml`. Update the config file if needed.

Now, you should have a mqtt service instance provisioned for you:
![Mosquitto in marketplace](images/tap-mosquitto-created.png)
 
# Binding the service to the app
There are several ways to indicate that given app should use certain service. 
When you have an app deployed, you could find it in `Applications` section (in TAP's console), display the details (`See details`) and use `Bindings` tab. Alternatively, you could do this using CLI. 

In this example we are using manifest file.


Manifests are the wey yhe platform let's you declare what you want to have when deploying the app. The project contains example manifest ([src/cloudfoundry/manifest.yml](../src/cloudfoundry/manifest.yml)):

 

```
-------
applications:
- name: mqtt-listener
  memory: 512M
  host: mqtt-listener
  path: target/mqtt-listener-${version}.jar
  services:
  - mqtt-listener-messages
``` 

In our case you see we wanted 512MB of memory for the app, named it mqtt-listener and showed that the app should use `mqtt-listener-messages` service.

# Deploying the application
In order to deploy the application on TAP you first need to build the project:

    mvn clean package
    
This produces jar file in target directory of the project. The name of the file is going to be mqtt-listener-${version}.jar, where ${version} is current project version (see [`version` property of pom.xml](../pom.xml)).

To deploy, you can use the CLI:

    cf push
    
It assumes you are logged in (cf login), and the current directory is the project root dir. This way `cf push` uses manifest.yml that contains all required information.


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

We use property file that recognizes env variables (see [application.yml](../src/application.yml)): 

```
services:
  mqtt:
    hostname: tcp://${vcap.services.mqtt-listener-messages.credentials.hostname:localhost}
    port: ${vcap.services.mqtt-listener-messages.credentials.port:1883}
    username: ${vcap.services.mqtt-listener-messages.credentials.username:mqtt-listener}
    password: ${vcap.services.mqtt-listener-messages.credentials.password:test}
    clientName: mqtt-listener
    topic: mqtt-listener/test-data
logging:
  level:
    org.trustedanalytics.mqttlistener: DEBUG
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