# artnet4j
Art-Net DMX over IP library for Java

## Features

* Reuse of the socket address

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