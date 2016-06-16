 
## Producing messages
To test if the application works as expected, some messages need to be sent to MQTT topic we configured for it.

To make this task easier, there is a script ([tools/publish_file.sh](../tools/publish_file.sh)) prepared for you. 

Create a test file (for example `test.txt`) and run:

```
run ./publish_file.sh text.txt
```

This will send the contents of the file, line by line with 1 second delay, to selected topic.

> Update `tools/publish_file.sh` and set host, port and topic name accordingly. You will also need user/password to connect to the broker. Get the info from the service binding (see [Deploying on TAP](Deploying-on-TAP.md#how-to-access-the-credentials-in-the-app)).


## Checking the output
In first version of the demo application only console consumer is provided. It means, in order to check the the outcome, we see console output.

Inspect the application logs to see if the messages were received.

You should get something similar to:

```
cf logs mqtt-listener 


[TODO paste real logs]
```

