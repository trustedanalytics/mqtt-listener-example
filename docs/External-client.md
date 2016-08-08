In order to connect from outside TAP using secured wire (SSL), you need to use platform's certificate in the client. 

## Certificate 

There are two possible sources from which you can obtain certificates needed to connect to Mosquitto.
Depending on the system configuration you are working on you may use: 

1. CA-signed certificate - a certificate issued by a trusted CA.

2. Self-signed certificate - a certificate generated during the deploy phase.


How to choose the proper source? 
* If the certificate was generated during the deploy phase you must obtain it directly from the MQTT server. 
* Otherwise it is better to use the CA-signed certificate located in the certificate repository on the MQTT client's machine.


### CA-signed certificate

A CA-signed certificate is usually shipped by the OS vendor together with other CA-signed certificates
and may be contained within a certificate chain bundle (a special file) on the local Linux machine.
That means it may already be found on the MQTT client's machine.

To use it we just need to point that file but its location depends on the OS. 
Below are locations on selected systems:

* Ubuntu:
    - /etc/ssl/certs/ca-certificates.crt

* Debian:
    - /etc/ssl/certs/ca-certificates.crt

* CentOS:
    - /etc/ssl/certs/ca-bundle.crt
    - /etc/pki/tls/certs/ca-bundle.crt

* Fedora:
    - /etc/pki/tls/certs/ca-bundle.crt


Unfortunately it may differ per server instance and needs to be verified.

Use this file in case the MQTT server has a signed certificate.


### Self-signed certificate

The certificate provided to the client may be the public certificate of the MQTT server. 
As far as I know, there is no way of obtaining it with the TAP console.
As the certificate is public, you can download it with the openssl command-line utility, as shown below:

```bash
#!/usr/bin/env bash

MQTT_SERVER=<<host>>
MQTT_PORT=<<port>>
CERT_OUTPUT_FILE=mqtt-cert.pem

echo | openssl s_client -connect ${MQTT_SERVER}:${MQTT_PORT} -showcerts 2>&1 | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > ${CERT_OUTPUT_FILE}
```

Some info on configuring certificate for Mosquitto client: http://stackoverflow.com/a/34333973
Some distributions of mosquitto-client don't support using certificate file. Make sure that you have the latest version.

## Connecting with CLI
If you want to connect to Mosquitto broker on secured port (this is how TAP exposes the service) with CLI, you need to provide server certificate,

```
mosquitto_sub -h mqtt.{HOST_DOMAIN} -p {PORT} -u {USER} -P {PASSWD} --cafile {MQTT_CERT} -t {MQTT_TOPIC}
mosquitto_pub -h mqtt.{HOST_DOMAIN} -p {PORT} -u {USER} -P {PASSWD} --cafile {MQTT_CERT} -t {MQTT_TOPIC} -m {MESSAGE}
```

where:
**MQTT_CERT** is a path to a certificate obtained as described in the previous section (see: [Certificate](External-client.md#certificate)) 

For example:

```
mosquitto_sub -h mqtt.demo.gotapaas.eu -p 32914 -u testuser -P testpassword --cafile mqtt-cert.pem -t mqtt-listener/test-data
mosquitto_pub -h mqtt.demo.gotapaas.eu -p 32914 -u testuser -P testpassword --cafile mqtt-cert.pem -t mqtt-listener/test-data -m test
```

Above will use the public certificate obtained from the MQTT server.

## Example client
After obtaining the credentials (see: [How to access the credentials in the app](Deploying-on-TAP.md#optional-verifying-mosquitto-service-metadata)) you could use a client similar to this one (Python pseudo code) to connect to TAP's MQTT: 

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