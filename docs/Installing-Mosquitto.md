For development purposes you may need to use Mosquitto locally. This page shows some example installation procedures.

## OS X
This example installation was tested on OS X El Capitan (10.11.4) and assumes using [`brew`](http://brew.sh)  
 
After running:
```brew install mosquitto```

The installer says:
```
mosquitto has been installed with a default configuration file.
You can make changes to the configuration by editing:
    /usr/local/etc/mosquitto/mosquitto.conf

To have launchd start mosquitto now and restart at login:
  brew services start mosquitto
Or, if you don't want/need a background service you can just run:
  mosquitto -c /usr/local/etc/mosquitto/mosquitto.conf
==> Summary
  /usr/local/Cellar/mosquitto/1.4.8: 32 files, 616.1K
```

Now we can modify the config (/usr/local/etc/mosquitto/mosquitto.conf) if needed and run the service:
```brew services start mosquitto```

### Testing the installation
This distribution of Mosquitto comes with `mosquitto_pub` and `mosquitto_sub` commands.


mosquitto_pub -t mqtt-listener/test-data -m "dfdafdsdafffdsaafds asfd dsaffasf afs fsadsdfassfad asdfsda" -u mqtt-listener -P test
mosquitto_sub -t +/+ -u mqtt-listener -P test

## Linux
[TBD]

## Docker 
[TBD]