# Lunar Lander

A simple Lunar Lander game made as my first Java project for classes on my university. 

## User guide

Download the repo (to run you only need to download .jar files).

Run:
```

$ cd YourDirectory

$ git clone https://github.com/zielinskimichal/Lunar-Lander.git

$ java -jar Client.jar

$ java -jar Server.jar

```

or double-click the .jar files to open client and server.

Please note you need to have JRE (Java Runtime Environment) installed.

## Player guide

To play the game type your nickname in the textfield and press the start button. Controlling the rocket is done by pressind w, s, a, d buttons on the keyboard. The game can be paused by pressing space.

You can connect to the server(if you turn it on) via the connect button in main menu. This allows the client to send your score which is then saved on the server. It also makes the client download levels configuration from the server rather than taking it from local config files. The port numbers are specified in "resources/port.properties" files of both client and server.  

Docummentation of the project is written in Polish since it was required to get the positive score from the project.
