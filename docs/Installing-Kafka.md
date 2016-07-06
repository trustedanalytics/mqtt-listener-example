## Local development

For local development Zookeeper and Kafka must be installed.

### Zookeeper

Zookeeper can be downloaded from Apache ZooKeeper Releases

Simple installation could look like this:

    cd ~/mqtt-listener-example
    mkdir zookeeper
    cd zookeeper
    wget http://apache.mirrors.lucidnetworks.net/zookeeper/zookeeper-3.4.6/zookeeper-3.4.6.tar.gz
    tar -xvf zookeeper-3.4.6.tar.gz
    cd zookeeper-3.4.6/

To test the installation:

    cp conf/zoo_sample.cfg conf/zoo.cfg
    bin/zkServer.sh start

You should see something similar to:

    JMX enabled by default
    Using config: /home/ubuntu/zookeeper-3.4.6/bin/../conf/zoo.cfg
    Starting zookeeper ... STARTED

### Kafka

Kafka downloads ara available here: http://kafka.apache.org/downloads.html. The current stable version is 0.10.0.0.

I pick version kafka_2.11-0.10.0.0.tgz from the following mirror: http://mirrors.sonic.net/apache/kafka/0.10.0.0/kafka_2.11-0.10.0.0.tgz

    cd ~/mqtt-listener-example
    mkdir kafka
    cd kafka
    wget http://mirrors.sonic.net/apache/kafka/0.10.0.0/kafka_2.11-0.10.0.0.tgz
    tar xvzf kafka_2.11-0.10.0.0.tgz  

To test if it works, start the server:

    cd kafka_2.11-0.10.0.0
    bin/kafka-server-start.sh config/server.properties


### Testing installation

##### Build and run application

    mvn clean package
    mvn spring-boot:run

##### Write to Mosqitto

To send a message from local instance of Mosquitto you can use its mosquitto_pub command:  
mosquitto_pub -t {TOPIC} -m {MESSAGE} -u {USER} -P {PASSWD}  
We use properties from file (see [application-default.yml](../src/application-default.yml)):

    mosquitto_pub -t mqtt-listener/test-data -m "testMessage..." -u mqtt-listener -P test

##### Check messages

Mosquitto messages:
    
    mosquitto_sub -t mqtt-listener/test-data  -u mqtt-listener -P test

Kafka messages:

    cd ~/mqtt-listener-example/kafka/bin
    ./kafka-console-consumer.sh --zookeeper localhost:2181 --topic inputTopic --from-beginning


