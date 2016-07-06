 
## Producing messages
To test if the application works as expected, some messages need to be sent to MQTT topic we configured for it.

To make this task easier, there is a script ([tools/publish_file.sh](../tools/publish_file.sh)) prepared for you. 

Create a test file (for example `test.txt`) and run publish script.
This will send the contents of the file, line by line with 1 second delay, to selected topic.


If you are testing remotely (for example, accessing TAP from your local machine - see [External-client](External-client.md)), run:
```
./publish_file.sh text.txt
```

If you are testing locally, run:
```
./publish_file_local.sh text.txt
```

> Update the script and set host, port and topic name accordingly. You will also need user/password to connect to the broker. Get the info from the service binding (see [Deploying on TAP](Deploying-on-TAP.md#optional-verifying-mosquitto-service-metadata)). `publish_file.sh` also assumes that certificate file is named mqtt-cert.pem.


## Checking the output
There are two ways of checking the outcome. 
The first is to use 'kafka console consumer'. To do this, you should be on the environment where Kafka is installed.
 
    ./kafka-console-consumer.sh --zookeeper {URL:PORT} --topic {TOPIC_NAME} --from-beginning
    
The second way is to check application logs.
 
```
cf logs mqtt-listener 
```

You should get something similar to:

```
2016-07-06T14:26:34.28+0200 [App/0]      OUT [2016-07-06 12:26:34.284] boot - 29 DEBUG [MQTT Call: mqtt-listener] --- OnMessageArrived: topic: mqtt-listener/test-data, message: test
2016-07-06T14:26:34.28+0200 [App/0]      OUT [2016-07-06 12:26:34.285] boot - 29 DEBUG [MQTT Call: mqtt-listener] --- KafkaConsumer: ---- sending message to Kafka
```

