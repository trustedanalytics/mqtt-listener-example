In order to connect from outside TAP using secured wire (SSL), you need to use platform's certificate in the client. 

## Certificate 

The certificate provided to the client is the public certificate of the server. As far as I know, there is no way of obtaining it with the TAP console.
As the certificate is public, you can download it with the openssl command-line utilility, as shown below:

```bash
#!/usr/bin/env bash

MQTT_SERVER=<<host>>
MQTT_PORT=<<port>>
CERT_OUTPUT_FILE=mqtt-cert.pem

echo | openssl s_client -connect ${MQTT_SERVER}:${MQTT_PORT} -showcerts 2>&1 | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > ${CERT_OUTPUT_FILE}
```

Some info on configuring certificate for Mosquitto client: http://stackoverflow.com/a/34333973

## Connecting with CLI
If you want to connect to Mosquitto broker on secured port (this is how TAP exposes the service) with CLI, you need to provide server certificate,

For example:
```
mosquitto_sub -h mqtt.demo.gotapaas.eu -p 32914 -u testuser -P testpassword --cafile mqtt-cert.pem -t mqtt-listener/test-data
mosquitto_pub -h mqtt.demo.gotapaas.eu -p 32914 -u testuser -P testpassword --cafile mqtt-cert.pem -t mqtt-listener/test-data -m test
````



## Example client
After obtaining the credentials (see: [How to access the credentials in the app](Deploying-on-TAP.md#how-to-access-the-credentials-in-the-app)) you could use a client similar to this one (Python pseudo code) to connect to TAP's MQTT: 

```python
MQTT_TOPIC_NAME = "mqtt-listener/test-data"
SERVER_CERTIFICATE = <<path to cert file>>

mqtt_port = <<PORT>>
mqtt_username = <<USER>>
mqtt_pwd = <<PASSWORD>>
mqtt_client = mqtt.Client()
mqtt_client.username_pw_set(mqtt_username, mqtt_pwd)
mqtt_client.tls_set(SERVER_CERTIFICATE, tls_version=ssl.PROTOCOL_TLSv1_2)
mqtt_server_address = <<mqtt.demo-gotapaas.com>>
mqtt_client.connect(mqtt_server_address, int(mqtt_port), 20)
mqtt_client.publish(MQTT_TOPIC_NAME, "my message")
```