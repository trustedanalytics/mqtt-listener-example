For development purposes you may need to use Mosquitto locally. This page shows some example installation procedures.

## OS X
This example installation was tested on OS X El Capitan (10.11.4) and assumes using [`brew`](http://brew.sh)  
 
After running:
```brew install mosquitto```

The installer shows some useful information (like the version, location of config file, starting command):
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

This distribution of Mosquitto comes with `mosquitto_pub` and `mosquitto_sub` commands.

## Linux
Find Linux installation instructions for your distribution on http://mosquitto.org/download/.

## Raspberry Pi
Mosquitto is available in Debian repository and can be installed on Raspbian with:

```
sudo apt-get install mosquitto
```

Please, visit http://repo.mosquitto.org/debian/readme.txt for more details.

If you want to use mosquitto_pub or _sub, you need to install mosquitto-clients, too:
```
 sudo apt-get install mosquitto-clients
```


## Docker
For docker distribution of Mosquitto you could use https://hub.docker.com/r/toke/mosquitto/:

```
docker run -ti -p 1883:1883 -p 9001:9001 toke/mosquitto
```


# Mosquitto command-line clients
Some distributions don't include command-line utils for Mosquitto (`mosquitto_pub` and `mosquitto_sub`), they are distributed as separate package `mosquitto-clients`.
You may need to install the package.


# Testing the installation
If you have `mosquitto_pub` and `mosquitto_sub` installed, you could test the installation by subscribing to a topic and then publishing to the topic (from the command line).

Run in one terminal:

    mosquitto_sub -t mqtt-listener/test-data  -u mqtt-listener -P test

This command subscribes to `mqtt-listener/test-data` topic. When a message is sent to the topic, it will be printed to the console.
Run in other terminal:

    mosquitto_pub -t mqtt-listener/test-data -m "dfdafdsdafffdsaafds asfd dsaffasf afs fsadsdfassfad asdfsda" -u mqtt-listener -P test

This command sends a message to `mqtt-listener/test-data` topic.

> Please mind, that the user (`-u` switch, and password `-P`) are defined for this example as described in ["Configuring security in Mosquitto"](Configuring-security-in-Mosquitto.md) doc. If you haven't configured it yet, get ird of `-u` `-P` switches.

