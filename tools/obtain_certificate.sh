#!/usr/bin/env bash

MQTT_SERVER=mqtt-listener.trusted-krb.gotapaas.eu
MQTT_PORT=33102
CERT_OUTPUT_FILE=mqtt-cert.pem

echo | openssl s_client -connect ${MQTT_SERVER}:${MQTT_PORT} -showcerts 2>&1 | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > ${CERT_OUTPUT_FILE}