# ArtNet for Java & Processing [![Build Status](https://travis-ci.org/cansik/artnet4j.svg?branch=master)](https://travis-ci.org/cansik/artnet4j) [![Build status](https://ci.appveyor.com/api/projects/status/811y7bud6srbdbny?svg=true)](https://ci.appveyor.com/project/cansik/artnet4j)

Art-Net DMX over IP library for Java and Processing

## Features

* Bind server to a custom network interface
* Reuse of the socket address

## Examples
### Bind Custom Network Interface
This is just a short example to show how to bind a custom network interface `en5` to the artnet server:

```java
NetworkInterface ni = NetworkInterface.getByName("en5");
InetAddress address = ni.getInetAddresses().nextElement();

artnet.start(address);
```

## Code
Check out the [processing](https://processing.org/) example:

* [examples/ArtNetTest](examples/ArtNetTest)

## About
Art-Net by Artistic Licence allows for broadcasting DMX data via IP/UDP. This library is implementing the basic protocol for Java applications.

Currently supported core features are:

* Device/node discovery & automatic updating of node configurations
* Java typed descriptors of nodes & node properties
* Abstracted DmxUniverse allowing direct pixel/channel access without having to deal with packets
* JAXB configuration support for storing universe/node descriptions as XML
* Listener support for various events
* Sending of DMX512 data via UDP broadcast or unicast

This project is currently still in pre-alpha stage, so currently only source access via hg. Be also aware that large parts of the codebase are still undergoing major changes.

Forked from [artnet4j](https://code.google.com/archive/p/artnet4j/).

New features are developed by Florian.
