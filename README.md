# ArtNet for Java & Processing [![Build Status](https://travis-ci.org/cansik/artnet4j.svg?branch=master)](https://travis-ci.org/cansik/artnet4j) [![Build status](https://ci.appveyor.com/api/projects/status/811y7bud6srbdbny?svg=true)](https://ci.appveyor.com/project/cansik/artnet4j)

Art-Net DMX over IP library for Java and Processing. This library adds a lot of features to the existing artnet4j project. Including support to read dmx data.

## Features

* Bind server to a custom network interface
* Reuse of the socket address
* Added ability to receive `OpDmx` packages
* Send and receive via UDP broadcast.
* Support for [Art-Ext](https://github.com/mattbeghin/Art-Ext-Poll) packages.

## Examples
The library adds a new class called `ArtNetClient`, which contains easy access to the underlaying Art-Net implementation.

### Send Dmx Data
To send dmx data you have to create a new client. It is possible to skip the buffer creation, because receiving is not needed here.

```java
byte[] dmxData = new byte[512];
ArtNetClient artnet = new ArtNetClient(null);
artnet.start();

// set data
dmxData[0] = 128;

// send data to localhost
artnet.unicastDmx("127.0.0.1", 0, 0, dmxData);

artnet.stop();
```
*Based on [SendDmxData](examples/SendDmxData/SendDmxData.pde)*

It is also possible to send the data via broadcast.

```java
// to broad cast data
artnet.broadcastDmx(0, 0, dmxData);
```

### Read Dmx Data
Reading data is simple as creating a new client, and read the bytes from the buffer. Please be aware that you have to mask the bytes with `0xFF` (because they are signed).

```java
ArtNetClient artnet = new ArtNetClient();
artnet.start();

byte[] data = artnet.readDmxData(0, 0);
System.out.println("First Byte: " + data[0] & 0xFF);
artnet.start.stop();
```

*Based on [ReceiveDmxData](examples/ReceiveDmxData/ReceiveDmxData.pde)*

### Bind Custom Network Interface
It is also possible to set a custom network interface. Here you see how to bind a custom network interface `en5` to the ArtNet server:

```java
NetworkInterface ni = NetworkInterface.getByName("en5");
InetAddress address = ni.getInetAddresses().nextElement();

artnet.start(address);
```

## About
The library is based on then [artnet4j](https://code.google.com/archive/p/artnet4j/) project.

Art-Net by Artistic Licence allows for broadcasting DMX data via IP/UDP. This library is implementing the basic protocol for Java applications.

Currently supported core features are:

* Device/node discovery & automatic updating of node configurations
* Java typed descriptors of nodes & node properties
* Abstracted DmxUniverse allowing direct pixel/channel access without having to deal with packets
* JAXB configuration support for storing universe/node descriptions as XML
* Listener support for various events
* Sending of DMX512 data via UDP broadcast or unicast

This project is currently still in pre-alpha stage, so currently only source access via hg. Be also aware that large parts of the codebase are still undergoing major changes.

New features are developed by [Florian](https://github.com/cansik).
