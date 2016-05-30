# Securing Mosquitto server
There are several things you can do to make Mosquitto more secure. 

Default config provides values that are convenient for development, but could impose a risk when used "in production" - for example, allows for anonymous access to the server (anybody can connect).

Mosquitto service provided on TAP has security features already turned on. The following description is intended to provide you with the information on how to configure similar security measures for development (for local/development installation to reflect TAP's configuration).
  
   

## Configure authentication

These docs describe Mosquitto configuration in details: http://mosquitto.org/man/mosquitto-conf-5.html#idm46192618766320, including security settings.

First, we need to **disable anonymous access**. There is a switch for this in config file:
```
allow_anonymous false
```


Then we **create a password file** by runing this from command line: `mosquitto_passwd -b /usr/local/etc/mosquitto/pwfile mqtt-listener test` (see https://mosquitto.org/man/mosquitto_passwd-1.html for details)


> You need to update the file path to fit your needs/installation. In our case (see [OS X section of installation doc](docs/Installing-Mosquitto.md)) the broker is placed in /usr/local/etc/mosquitto/ so we have config and password files there.

And update *the config to use the password file*:
```
password_file /usr/local/etc/mosquitto/pwfile
```

The options above were put together in security.conf that is included for reference in this project.


## Network encryption
[TBD]