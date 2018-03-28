# Chat-IRC

Java chat almost IRC compliant with graphical interface. The GUI was written using Java Swing mainly, although the main chat area was styled using HTML+CSS for extra control.


## Running
First launch the server: ChatServer.jar:

```java
java -jar ChatServer.jar [port]
```

Once it's running execute the client (ClienteChat), you will have to build it first. Write the server IP and port where the server is running, and that should be it.

## Screenshots

![Main GUI](https://raw.githubusercontent.com/aurbano/irc-gui-java/master/img/screenshots/main.png)

> View of the main GUI, when connected directly to the server

![Commands](https://raw.githubusercontent.com/aurbano/irc-gui-java/master/img/screenshots/commands.png)

> Command list and description. The GUI implements all the commands in the Menus and different controls, so joining/creating a room, seeing online users... is all done automatically.

## Extras
Since this was a University project, we were all creating our own different clients. For a bit of extra fun I wrote [a DoS tool](https://github.com/aurbano/Chatter) that could take down any chat server within seconds :)

## Contributors

* [Alejandro U. Álvarez](http://urbanoalvarez.es)
* [Irene Martínez de Soto](https://github.com/irenemds)

## License
Licensed under the GPL license

[![Analytics](https://ga-beacon.appspot.com/UA-3181088-16/irc-gui-java/readme)](https://github.com/aurbano)
