import ch.bildspur.artnet.*;

ArtNetClient artnet;
byte[] dmxData = new byte[512];

void setup()
{
  size(500, 250);
  
  colorMode(HSB, 360, 100, 100);
  textAlign(CENTER, CENTER);
  textSize(20);

  // create artnet client with specficis ports (server / client)
  artnet = new ArtNetClient(new ArtNetBuffer(), 8000, 8000);
  artnet.start();
}

void draw()
{
  // create color
  int c = color(frameCount % 360, 80, 100);

  background(c);

  // fill dmx array
  dmxData[0] = (byte) red(c);
  dmxData[1] = (byte) green(c);
  dmxData[2] = (byte) blue(c);

  // send dmx to localhost
  artnet.unicastDmx("127.0.0.1", 0, 0, dmxData);

  // show values
  text("R: " + (int)red(c) + " Green: " + (int)green(c) + " Blue: " + (int)blue(c), width / 2, height / 2);
}
