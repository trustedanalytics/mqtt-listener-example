For this application we chose to use [paho](https://eclipse.org/paho/) which is an iot.eclipse.org project.
![](https://eclipse.org/paho/images/paho_logo_400.png)

Pahoo provides documentation, examples, utilities and MQTT clients for different languages. 
We'll take their [Java Client](https://github.com/eclipse/paho.mqtt.java) which is mature and commonly used.

## Connecting to MQTT broker
First we need to connect to MQTT. It could be achieved with following code fragment:
```Java
MqttConnectOptions connectOptions = new MqttConnectOptions();
connectOptions.setUserName("myuser");
connectOptions.setPassword("mypassword".toCharArray());
connectOptions.setCleanSession(false);
client = new MqttClient("tcp://mymqtthost.com:1833", "myClientId", new MemoryPersistence());
client.connect(connectOptions);
```

The key element here is MqttClient. Find detailed description of the class here: [MqttClient(String, String, MqttClientPersistence)](https://www.eclipse.org/paho/files/javadoc/org/eclipse/paho/client/mqttv3/MqttClient.html) 

> In order to connect to the broker you need to know the server URI, username and password for the connection, choose a client identification string and decide on connection persistance. Visit application.yml and ["Deploying on TAP"](docs/Deploying-on-TAP.md)  to see what values we'll use in this example and we're going to obtain them.
 
## Publishing messages
After obtaining the connection, in order to publish a message to selected topic you could do the following:
```Java
String content = "hello, world!";
String topic = "myTopic";
int qos = 2;
MqttMessage message = new MqttMessage(content.getBytes());
message.setQos(qos);
client.publish(topic, message);
```

## Receiving messages 
The authors of Paho client created a callback mechanism ([MqttCallback](https://www.eclipse.org/paho/files/javadoc/org/eclipse/paho/client/mqttv3/MqttCallback.html)) that enables applications to be notified when one of these asynchronous events occur:

 * a connection to server was lost
 * sending a message has been completed
 * new message arrived from the server

You can use the interface to provide your own handler by overriding selected methods:

| method | purpose |
|---|---|
| connectionLost  |  called when the connection to the server was lost  |
| deliveryComplete| called when delivery for a message has been completed (all acknowledgments have been received) |
| messageArrived  | called when new message arrived from the server |


You could create the following class:

```Java
public class MyCallback implements MqttCallback {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyCallback.class);
    @Override public void connectionLost(Throwable throwable) {
        LOGGER.error("Connection lost. " + throwable);
    }

    @Override public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        LOGGER.debug("topic: {}, message: {}", topic, mqttMessage);
        // **Do something with the message**
    }

    @Override public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        LOGGER.debug("deliveryComplete: {}",  iMqttDeliveryToken);
    }
}
```

To use this as the callback, you need to register the callback **before** connecting to the server, connect, then subscribe to topic(s):

```Java
    MqttConnectOptions connectOptions = new MqttConnectOptions();
    connectOptions.setUserName("myuser");
    connectOptions.setPassword("mypassword".toCharArray());
    connectOptions.setCleanSession(false);
    client = new MqttClient("tcp://mymqtthost.com:1833", "myClientId", new MemoryPersistence());
    
    MyCallback callback = new MyCallback();
    client.setCallback(callback);
    client.connect(connectOptions);
    client.subscribe("myTopic");
```

